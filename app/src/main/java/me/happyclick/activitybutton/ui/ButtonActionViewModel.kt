package me.happyclick.activitybutton.ui

import android.content.Context
import com.ww.roxie.BaseViewModel
import com.ww.roxie.Reducer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import me.happyclick.activitybutton.model.ActionModel
import java.util.*

class ButtonActionViewModel(
    initialState: ButtonActionState?,
    private val buttonActionUseCase: ButtonActionUseCase,
    private val context: Context
) : BaseViewModel<ButtonAction, ButtonActionState>() {

    override val initialState = initialState ?: ButtonActionState(activity = false)

    private val reducer: Reducer<ButtonActionState, ButtonActionChange> = { state, change ->
        when (change) {
            is ButtonActionChange.Loading -> state.copy(
                activity = true
            )
            is ButtonActionChange.ActionList -> state.copy(
                activity = false,
                action = change.list
            )
        }
    }

    init {
        bindActions()
    }

    private fun bindActions() {
        val getActionChange = actions
            .ofType<ButtonAction.GetActionButtonClicked>()
            .switchMap { click ->
                buttonActionUseCase.getActions(context)
                    .subscribeOn(Schedulers.io())
                    .toObservable()
                    .map<ButtonActionChange> {
                        val filteredActions = filterActions(it)
                        ButtonActionChange.ActionList(filteredActions)
                    }
                    .doOnError { // here should be some error function - show a toast or whatever
                    }
                    .startWith(ButtonActionChange.Loading)
            }

        disposables += getActionChange
            .scan(initialState, reducer)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(state::setValue)
    }

    private fun filterActions(actions: List<ActionModel>): List<ActionModel> {
        val day =
            (Calendar.getInstance(TimeZone.getDefault()).get((Calendar.DAY_OF_WEEK)) - 1).toShort()
        return actions.filter { actionModel ->
            actionModel.enabled && actionModel.valid_days.contains(
                day
            )
        }
            .sortedBy { actionModel -> actionModel.priority }
    }

}