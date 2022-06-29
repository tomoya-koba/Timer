package jp.techacademy.tomoya.kobayashi5.timer

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

//Beginning of Permission
    private val PERMISSIONS_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        permission.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("ANDROID", "許可されている")
                } else {
                    Log.d("ANDROID", "許可されていない")
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSIONS_REQUEST_CODE
                    )
                }
            }
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
                    Log.d("ANDROID", "許可された")
                } else {
                    Log.d("ANDROID", "許可されなかった")
                }
        }
//End of Permission ※ここまでは絶対OK
//Beginning of OnClick Method

        override fun onClick(v: View) {
            when (v.id) {
                R.id.previous_button    -> showPreviousImg()
                R.id.next_button        -> showNextImg()
                R.id.play_button        -> playImg()
            }
        }


        // 質問事項
        // fun onClick...  ==  previous_button.setOnClickListener{... ?
        // ここにfunctionの定義を入れても大丈夫か？

//End of OnClick Method


        mTimer.cancel()


        //ここからが自作関数の領域

//        案①
//        previous_buttonを押したらmoveToPrevious()で前の画像を表示させる。
//        最初に戻ったら（cursor.moveToFirstになったら）closeさせないで、moveToLast()で最後に戻す。
//        これを繰り返せば、画像がぐるぐると表示させられる。
//        NextはNextで、これの逆の設定を行う。

//            カーソルをメンバ変数　ー＞　closeはアプリが終了するときのみ。
        //メンバ変数化*****
//            カーソルがNULLじゃないときにclose処理をするように記述

//        案②
//        最初に取得した画像の数を取得する。　※画像の個数.Long.size()だとか、GetCount()みたいな関数があるのではないか？
//        Arrayか、Map（id,URI）で画像を扱う。
//        previous_buttonを押したら配列の1個前のidを参照する。（　id-1　を計算する）
//        Array[0]まで参照したら、その次のアクションでは配列の一番最後に飛ばす。　
//            ※このときに取得した画像数を把握しておく必要あり
//        これを繰り返せば、画像がぐるぐると表示させられる。
//        IMG id = +=1をnext_button、　−＝1をprevious_buttonみたいな感じにする？


//Beginning of ShowPreviousImg() ・・・1つ前の画像を表示させる
        private fun showPreviousImg() {
    val resolver = contentResolver
    val cursor = resolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        null,
        null,
        null,
        null
    )

            if (cursor!!.moveToFirst()) {
                do {
                    val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    Log.d("ANDROID", "URI : " + imageUri.toString())
                } while (cursor.moveToPrevious())
            }
            cursor.close()
        }

//End of ShowPreviousImg()
//Beginning of ShowNextImg()　・・・次の画像を表示させる
        private fun showNextImg() {
            val resolver = contentResolver
            val cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
            )

            if (cursor!!.moveToFirst()) {
                do {
                    val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    Log.d("ANDROID", "URI : " + imageUri.toString())
                } while (cursor.moveToNext())
            }
            cursor.close()
        }

//End of ShowNextImg()
//Beginning of PlayImg()・・・一時再生＆ボタンのテキストを再生->一時停止と切り替える
        private fun playImg() {

        }

//End of PlayImg()
    }
}









//プロジェクトを新規作成し、 AutoSlideshowApp というプロジェクト名をつけてください
// -> OK

//スライドさせる画像は、Android端末に保存されているGallery画像を表示させてください（つまり、ContentProviderの利用）
// -> OK

//画面にはImageViewと3つのボタン（進む、戻る、再生/停止）を配置してください
// -> OK

//ユーザがパーミッションの利用を「拒否」した場合にも、アプリの強制終了やエラーが発生しないようにしてください。
//- -> OK



//進むボタンで1つ先の画像を表示し、戻るボタンで1つ前の画像を表示します
//最後の画像の表示時に、進むボタンをタップすると、最初の画像が表示されるようにしてください
//最初の画像の表示時に、戻るボタンをタップすると、最後の画像が表示されるようにしてください
//再生ボタンをタップすると2秒後に自動送りが始まり、2秒毎にスライドさせてください
//再生ボタンをタップすると、ボタンの表示が「停止」になり、停止ボタンをタップするとボタンの表示が「再生」になるようににしてください
//停止ボタンをタップすると自動送りが止まり、進むボタンと戻るボタンをタップ可能にしてください
//自動送りの間は、進むボタンと戻るボタンはタップ不可にしてください


