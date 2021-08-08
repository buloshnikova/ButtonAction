package me.happyclick.activitybutton.ui.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import me.happyclick.activitybutton.Activity
import me.happyclick.activitybutton.R


class ContactsFragment : Fragment(),
    LoaderManager.LoaderCallbacks<Cursor>,
    AdapterView.OnItemClickListener {

    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100

    @SuppressLint("InlinedApi")
    private val FROM_COLUMNS: Array<String> = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.LOOKUP_KEY,
        ContactsContract.Contacts.DISPLAY_NAME)

    @SuppressLint("InlineApi")
    private val PROJECTION: Array<out String> = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.LOOKUP_KEY,
        ContactsContract.Contacts.DISPLAY_NAME )

    @SuppressLint("InlinedApi")
    private val SELECTION: String = "${ContactsContract.Contacts.DISPLAY_NAME} LIKE ?"

    private val TO_IDS: IntArray = intArrayOf(android.R.id.text1)
    // _ID column
    private val CONTACT_ID_INDEX: Int = 0
    // CONTACT_KEY column
    private val CONTACT_KEY_INDEX: Int = 1

    lateinit var contactsList: ListView
    var contactId: Long = 0
    var contactKey: String? = null
    var contactUri: Uri? = null
    private var cursorAdapter: SimpleCursorAdapter? = null

    companion object {
        fun newInstance() = ContactsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loaderManager.initLoader(0, null, this)
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contacts_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.also {
            showContacts(it)
//            contactsList = it.findViewById<ListView>(android.R.id.list)
//            cursorAdapter = SimpleCursorAdapter(
//                it,
//                R.layout.contacts_list_item,
//                null,
//                FROM_COLUMNS, TO_IDS,
//                0
//            )
//            contactsList.adapter = cursorAdapter
        }
        contactsList.onItemClickListener = this
    }

    override fun onCreateLoader(loaderId: Int, args: Bundle?): Loader<Cursor> {
        val selectionArgs = emptyArray<String>()
        return activity?.let {
            return CursorLoader(
                it,
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                SELECTION,
                selectionArgs,
                null
            )
        } ?: throw IllegalStateException()
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {
        cursorAdapter?.swapCursor(cursor)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        cursorAdapter?.swapCursor(null)
    }

    override fun onItemClick(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val cursor: Cursor? = (parent.adapter as? CursorAdapter)?.cursor?.apply {
            moveToPosition(position)
            contactId = getLong(CONTACT_ID_INDEX)
            contactKey = getString(CONTACT_KEY_INDEX)
            contactUri = ContactsContract.Contacts.getLookupUri(contactId, contactKey)
        }
    }

    private fun showContacts(activity: FragmentActivity) {
        contactsList = activity.findViewById<ListView>(android.R.id.list)
        cursorAdapter = SimpleCursorAdapter(
            activity,
            R.layout.contacts_list_item,
            null,
            FROM_COLUMNS, TO_IDS,
            0
        )
        contactsList.adapter = cursorAdapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                activity?.let {
                    showContacts(it)
                }
            } else {
                Toast.makeText(
                    this.context!!,
                    "Until you grant the permission, we can not display the names",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}