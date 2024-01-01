package com.example.week1

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.init_screen)

        requestContactsPermission()
        requestStoragePermission()
    }

    private val CONTACTS_PERMISSION_REQUEST_CODE = 101
    private val STORAGE_PERMISSION_REQUEST_CODE = 100
    private fun requestContactsPermission() {
        val permission = Manifest.permission.READ_CONTACTS
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // 권한이 이미 허용된 경우 연락처 가져오기
                fetchContacts()
                Handler().postDelayed({
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // 여기에 실행할 코드 작성
                }, 3000)
            }
            else -> {
                // 권한 요청
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    CONTACTS_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun requestStoragePermission() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
//                Toast.makeText(this, "저장공간 접근이 허용되었습니다", Toast.LENGTH_SHORT).show()
//                loadimages()
            }
            else -> {
                // 권한 요청
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    STORAGE_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACTS_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchContacts()

                Handler().postDelayed({
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // 여기에 실행할 코드 작성
                }, 3000)
            } else {
                finish()
            }
        }
    }

    @SuppressLint("Range")
    private fun fetchContacts() {

        lifecycleScope.launch(Dispatchers.IO) {
            val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
            )

            cursor?.use {
                while (it.moveToNext()) {
                    var name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    var number = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    add_contact(name, number)
                }
            }
        }
    }
}