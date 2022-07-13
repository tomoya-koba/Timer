package jp.techacademy.tomoya.kobayashi5.timer

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.*
import android.provider.MediaStore
import android.content.ContentUris
import android.database.Cursor
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    //メンバ変数を作成
    private var mCursor: Cursor? = null    //NULL許容型で宣言
    private var mTimer: Timer? = null       //NULL許容型で宣言
    private var mHandler = Handler()        //UIに指示を出させるために必要

    //Beginning of Permission
    private val PERMISSIONS_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        previous_button.setOnClickListener(this)
        next_button.setOnClickListener(this)
        play_button.setOnClickListener(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getContentsInfo()
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
            }
        } else {
            getContentsInfo()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }
        }
    }

//End of Permission ※ここまでは絶対OK
//Beginning of getContentsInfo Method　・・・起動＆パーミッションされたら画像を表示させる。

    private fun getContentsInfo() {
        val resolver = contentResolver
        mCursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
            null, // 項目（null = 全項目）
            null, // フィルタ条件（null = フィルタなし）
            null, // フィルタ用パラメータ
            null // ソート (nullソートなし）
        )
        if (mCursor!!.moveToFirst()) {
            // indexからIDを取得し、そのIDから画像のURIを取得する
            val fieldIndex = mCursor!!.getColumnIndex(MediaStore.Images.Media._ID)
            val id = mCursor!!.getLong(fieldIndex)
            val imageUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            imageView.setImageURI(imageUri)
        }
    }

    private fun showContentsInfo() {
        val fieldIndex = mCursor!!.getColumnIndex(MediaStore.Images.Media._ID)
        val id = mCursor!!.getLong(fieldIndex)
        val imageUri =
            ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
        imageView.setImageURI(imageUri)
    }





//Beginning of OnClick Method
    override fun onClick(v: View) {
        when (v.id) {
            R.id.previous_button -> showPreviousImg()
            R.id.next_button -> showNextImg()
            R.id.play_button -> playImg()
        }
    }

    private fun showPreviousImg() {
        if (mCursor != null) {
            if (mCursor!!.moveToPrevious()) { //最初まで戻ってるなら一番最後に
                showContentsInfo()
            } else {
                mCursor!!.moveToLast()
                showContentsInfo()
            }
        }
    }


    private fun showNextImg() {
        if (mCursor != null) {
            if (mCursor!!.moveToNext()) { //最初まで戻ってるなら一番最後に
                showContentsInfo()
            } else {
                mCursor!!.moveToFirst()
                showContentsInfo()
            }
        }
    }


    private fun playImg() {
            if (mCursor != null && mTimer == null) {
                previous_button.isEnabled = false
                next_button.isEnabled = false
                play_button.text = "停止"

                mTimer = Timer()
                mTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        mHandler.post {
                            if (mCursor!!.moveToNext()) {
                                showContentsInfo()
                            } else {
                                mCursor!!.moveToFirst()
                                showContentsInfo()
                            }
                        }
                    }
                }, 2000, 2000) // 最初に始動させるまで2000ミリ秒、ループの間隔を2000ミリ秒 に設定
            } else {
                previous_button.isEnabled = false
                next_button.isEnabled = false
                play_button.text = "再生"
                mTimer!!.cancel()
            }


    }


    override fun onDestroy() {
        if (mCursor != null) {
            super.onDestroy()
            mCursor!!.close()
        }
    }

}
