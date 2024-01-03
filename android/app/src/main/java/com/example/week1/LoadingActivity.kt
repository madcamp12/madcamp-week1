package com.example.week1

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoadingActivity : ComponentActivity() {
    private val PROGRESS_UPDATE_INTERVAL = 30L // Progress 업데이트 주기 (단위: 밀리초)
    private val TOTAL_PROGRESS = 100 // ProgressBar의 최대 값
    private var currentProgress = 0 // 현재 Progress 값

    private val handler = Handler()
    private val progressRunnable = object : Runnable {
        override fun run() {
            if (currentProgress <= TOTAL_PROGRESS) {
                // Progress 업데이트
                progressBar.progress = currentProgress
                currentProgress++
                handler.postDelayed(this, PROGRESS_UPDATE_INTERVAL)
            } else {
                // Progress가 최대 값에 도달하면 MainActivity로 이동
                val intent = Intent(this@LoadingActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.init_screen)

        progressBar = findViewById(R.id.progressBar)
        progressBar.max = TOTAL_PROGRESS // Progress의 최대 값 설정

        requestContactsPermission()
    }

    private val CONTACTS_PERMISSION_REQUEST_CODE = 101
    private fun requestContactsPermission() {
        val permission = Manifest.permission.READ_CONTACTS
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // 권한이 이미 허용된 경우 연락처 가져오기
                fetchContacts()
                handler.post(progressRunnable) // Runnable 시작
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACTS_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchContacts()
                handler.post(progressRunnable) // Runnable 시작
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
                    val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val number = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val photoUri = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI))

                    var uri: String
                    if(photoUri == null){
                        uri = "None"
                    }else{
                        uri = photoUri
                    }

                    add_contact(name, number, uri)
                }
            }
        }
    }
}