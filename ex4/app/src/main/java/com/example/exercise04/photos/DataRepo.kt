package com.example.exercise04.photos


import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.example.exercise04.BuildConfig
import java.io.File

class DataRepo {
    public fun getListSize(): Int {
        return this.itemList?.size!!
    }

    fun addDataItem(item: DataItem) {
        this.itemList?.add(item)
    }

    private fun getSharedList(): MutableList<DataItem>? {
        if (this.itemList?.size != 0)
            return this.itemList

        this.itemList?.clear()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentResolver: ContentResolver =
            ctx.contentResolver // requireContext().contentResolver
        val cursor = contentResolver.query(
            uri,
            null,
            null, null,
            "${MediaStore.MediaColumns.DATE_TAKEN} DESC"
        )

        if (cursor == null) { // Error (e.g. no such volume)
        } else if (!cursor.moveToFirst()) { // no media in specified store
        } else {
            val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            do {
                var thisId = cursor.getLong(idColumn)
                var thisName = cursor.getString(nameColumn)
                var thisContentUri = ContentUris.withAppendedId(uri, thisId)
                var thisUriPath = thisContentUri.toString()
                this.itemList?.add(
                    DataItem(
                        this.itemList?.size!!,
                        thisName,
                        thisUriPath,
                        "No path yet",
                        thisContentUri
                    )
                )
            } while (cursor.moveToNext())
            cursor.close()
        }
        Log.d("myTag", "getSharedList: ${this.itemList?.size}")
        return this.itemList
    }


    private fun getAppList(): MutableList<DataItem>? {
        this.itemList?.clear()
        val dir: File? =
            ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        dir?.listFiles()
        this.itemList?.clear()
        if (dir?.isDirectory() == true) {
            var fileList = dir.listFiles()
            if (fileList != null) {
//                fileList.sortByDescending { it.lastModified() }
                for (value in fileList) {
                    var fileName =
                        value.name
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(
                            ".png"
                        ) || fileName.endsWith(".gif")
                    ) {
                        val tmpUri = FileProvider.getUriForFile(
                            ctx,
                            "${BuildConfig.APPLICATION_ID}.provider",
                            value
                        )
                        this.itemList?.add(
                            DataItem(
                                this.itemList?.size!!,
                                fileName,
                                value.toURI().path,
                                value.absolutePath,
                                tmpUri
                            )
                        )
                    }
                }
            }
        }
        val size = this.itemList?.size
        Log.d("PhotoListFragment", "get app list:" + size.toString())
        return this.itemList
    }


    fun setStorage(storage: Int): Boolean {
        if (storage != SHARED_S && storage != PRIVATE_S)
            return false
        photo_storage = storage
        return true
    }

    fun getList(): MutableList<DataItem>? {
        if (photo_storage == SHARED_S)
            return getSharedList()
        else
            return getAppList()
    }

    var itemList: MutableList<DataItem>? = mutableListOf<DataItem>()

    companion object {
        private var INSTANCE: DataRepo? = null
        private lateinit var ctx: Context
        val SHARED_S = 1
        val PRIVATE_S = 2


        fun getinstance(ctx: Context): DataRepo {
            if (INSTANCE == null) {
                INSTANCE = DataRepo()
                this.ctx = ctx
            }
            return INSTANCE as DataRepo
        }

        var photo_storage = SHARED_S

        fun getStorage(): Int {
            return photo_storage
        }
    }
}