package com.example.second

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tt1: EditText = findViewById(R.id.Text1)
        val tt2: EditText = findViewById(R.id.Text2)
        val bb1: Button =  findViewById(R.id.button)
        val bb2: Button =  findViewById(R.id.button2)
        val bb3: Button =  findViewById(R.id.button3)



        //val shareIntent = Intent.createChooser(sendIntent, null)
        //startActivity(shareIntent)



        tt1.setSingleLine(false)
        //tt1.setHorizontallyScrolling(false)
        tt1.setText("中")
        tt2.setSingleLine(false)
        //tt2.setHorizontallyScrolling(false)


        bb1.setOnClickListener {
            //tt2.text = tt1.text
            //tt2.setText("1" + tt1.text)
            tt2.setText(tt1.text.toString().aesEncrypt().toString())
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, tt1.text.toString().aesEncrypt().toString())
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

            tt1.setText("")
            Toast.makeText(this, "I solemnly swear that I am up to no good.", Toast.LENGTH_SHORT).show();
        }

        bb2.setOnClickListener {
            //tt2.text = tt1.text
            //tt2.setText("1" + tt1.text)

            tt1.setText(tt2.text.toString().aesDecrypt().toString())
            Toast.makeText(this, "I solemnly swear that I am up to no good.", Toast.LENGTH_SHORT).show();
        }
        bb3.setOnClickListener {



        }

    }
    object AESCrypt {

        // 記得定義一下你的 key
        //private const val key: String = "Your AES Key"
        private const val key: String = "12345678901234567890123456789012"
        // 這裡是宣告加解密的方法
        private const val transformation = "AES/CBC/PKCS5Padding"

        private val keySpec = SecretKeySpec(key.toByteArray(), 0, 32, "AES")
        private val ivParameterSpec = IvParameterSpec(key.toByteArray(), 0, 16)
        private val ByteArray.asHexUpper: String
            inline get() {
                return this.joinToString(separator = "") {
                    String.format("%02X", (it.toInt() and 0xFF))
                }
            }
        private val String.hexAsByteArray: ByteArray
            inline get() {
                return this.chunked(2).map {
                    it.uppercase(Locale.US).toInt(16).toByte()
                }.toByteArray()
            }


        // 加密使用的方法
        fun encrypt(input: String): String {
            val cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec)
            val encrypt = cipher.doFinal(input.toByteArray())
            return encrypt.asHexUpper
        }

        // 解密使用的方法
        fun decrypt(input: String): String {
            val cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec)
            val encrypt = cipher.doFinal(input.hexAsByteArray)
            return String(encrypt)
        }
    }
    fun String.aesEncrypt(): String = AESCrypt.encrypt(this)
    fun String.aesDecrypt(): String = AESCrypt.decrypt(this)

}