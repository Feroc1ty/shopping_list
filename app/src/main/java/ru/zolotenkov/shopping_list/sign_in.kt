package ru.zolotenkov.shopping_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.zolotenkov.shopping_list.databinding.ActivitySignInBinding

class sign_in : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    lateinit var launcher: ActivityResultLauncher<Intent>       //Создаем лаунчер типа интент
    lateinit var auth: FirebaseAuth                      //Класс для авторизации в Firebase разными способами. Сейчас через гугл аккаунт

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth                                   //Инициализируем переменную с Firebase
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){     //2. Инициализируем лаунчер. Он ждёт результат. Клиента которого мы выбрали
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)                          // Передаём интент нашего аккаунта, и через эту функцию достается наш аккаунт из интента
            try {                                                                           // Try-catch блок пделает проверку на ошибки
                val account = task.getResult(ApiException::class.java)                      //Достаем наш аккаунт
                if (account != null) {
                    firebaseAuthWithGoogle(account.idToken!!)                               //Если все ок то передаем айди токен (наш аккаунт) в функцию связывания с Firebase
                }

            }   catch (e: ApiException) {                                                   //Если ловим ошибку то пишем в лог об этом
                Log.d("MyLog", "Api Error, Empty Account")
            }
        }


        binding.bSignIn.setOnClickListener {      //По кнопке вызываем всю эту цепочку функций для авторизации
            signInWithGoogle()

        }

        checkAuthState()                            //Проверка на авторизацию при запуске активити


    }
    private fun getClient(): GoogleSignInClient {                    //Функция авторизации клиента через гугл аккаунт
                                                                    //Мы передаем этот клиент гуглу чтобы он выдал все авторизованные аккаунты на устройстве
        val gsio = GoogleSignInOptions                               //Чтобы мы могли из них выбрать
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, gsio)
    }

    private fun signInWithGoogle(){                     //1. Создаём выбранного клиента которого мы выбрали в окошке выбора аккаунтов
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)      //отправляем гуглу через лаунчер наш интент signInClient с выбранным ранее клиентом.
    }

    private fun firebaseAuthWithGoogle(idToken: String){                                //3.Связывает гугл аккаунт с файрбейс
        val credencial = GoogleAuthProvider.getCredential(idToken, null)      //Получаем credencial (сертификат) для авторизации. Типа ссылка на гугл аккаунт
        auth.signInWithCredential(credencial).addOnCompleteListener {                   //Передаём файрбейсу наш аутх.
            if (it.isSuccessful){
                Log.d("MyLog", "Google Auth is ok. Welcome")                          //Через addOnCompleteListener идёт проверка все ли ок
                checkAuthState()
            }
            else Log.d("MyLog", "Something goes wrong")
        }
    }
    private fun checkAuthState(){
        if (auth.currentUser != null){                                          //Проверка если юзер есть в нашей файрбазе то заходим
            val i = Intent(this, MainActivity::class.java)      //Открываем наше мейн активити
            startActivity(i)
        }

    }
}