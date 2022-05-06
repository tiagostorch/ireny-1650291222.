package com.ireny.nf_control_mei.ui.register_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ireny.nf_control_mei.R
import com.ireny.nf_control_mei.data.room.entities.User
import com.ireny.nf_control_mei.data.repositories.UserRepository
import com.ireny.nf_control_mei.data.repositories.Result
import kotlinx.coroutines.launch
import java.lang.Exception

class RegisterUserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _registerUserFormState = MutableLiveData<RegisterUserFormState>()
    val registerUserFormState: LiveData<RegisterUserFormState> = _registerUserFormState

    private val _userResult = MutableLiveData<UserResult>()
    val userResult: LiveData<UserResult> = _userResult

    private fun getProviderInfo(): ProviderInfo? {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val externalProvider = user.providerData.firstOrNull { el -> !el.providerId.contains(it.providerId)}
            var provider =  it.providerId

            externalProvider?.let {
                provider = externalProvider.providerId
            }
            return ProviderInfo(uid = it.uid, provider = provider)
        }
        return null

    }

    fun save(username: String,
             password: String,
             name: String,
             companyIdentify: String,
             companyName: String,
             phone: String){

        runCatching {
            val provider = getProviderInfo()
            provider?.let {
                if (provider.isExternalProvider()) {
                    completeSave(
                        username = username,
                        password = null,
                        name = name,
                        companyName = companyName,
                        companyIdentify = companyIdentify,
                        phone = phone,
                        providerInfo = provider
                    )
                    return
                }

                if (password.isNotBlank()) {
                    FirebaseAuth.getInstance().currentUser?.updatePassword(password)
                    completeSave(
                        username = username,
                        password = password,
                        name = name,
                        companyName = companyName,
                        companyIdentify = companyIdentify,
                        phone = phone,
                        providerInfo = provider
                    )
                    return
                }
                throw Exception("No password")
            }

            throw Exception("No logged")

        }.getOrElse {
            _userResult.value =
                UserResult(error = R.string.register_username_and_password_error)
        }
    }

    private fun completeSave(username: String,
                     password: String?,
                     name: String,
                     companyIdentify: String,
                     companyName: String,
                     phone: String,
                     providerInfo: ProviderInfo){

        val user = createLocalUser(
            username = username,
            password = password,
            name = name,
            companyName = companyName,
            companyIdentify = companyIdentify,
            phone = phone,
            uid = providerInfo.uid,
            provider = providerInfo.provider
        )

        insert(user)

        getUserByUid(providerInfo.uid)
    }

    private fun createLocalUser(username: String,
                           password: String?,
                           name: String,
                           companyIdentify: String,
                           companyName: String,
                           phone: String,
                           uid: String,
                           provider: String
    ): User {
        return User(
            uid = uid,
            provider = provider,
            email = username,
            password = password,
            name = name,
            companyName = companyName,
            companyIdentifier = companyIdentify,
            phone = phone
        )
    }

    private fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }

    fun getUserByUid(){
        val providerInfo = getProviderInfo()
        if(providerInfo != null){
            getUserByUid(providerInfo.uid)
        }else{
            _userResult.value = UserResult(error = R.string.user_not_found)
        }
    }

    private fun getUserByUid(uid:String){
        viewModelScope.launch {
            val result = repository.getBy(uid)
            if (result is Result.Success) {
                _userResult.value = UserResult(success = result.data)
            } else {
                _userResult.value = UserResult(error = R.string.user_not_found)
            }
        }
    }

    fun isCredentialsRegister(): Boolean {
        val providersInfo = getProviderInfo()
        providersInfo?.let {
            return !providersInfo.isExternalProvider()
        }
        return false
    }

    fun formDataChanged(password: String,
                        passwordRepeat: String,
                        name: String,
                        companyIdentify: String,
                        companyName: String,
                        phone: String)
    {
        val state = RegisterUserFormState()
        var isValid = true

        if(name.isBlank()){
            state.usernameError = R.string.required_field
            isValid = false
        }

        if(companyIdentify.isBlank()){
            state.companyIdentifyError = R.string.required_field
            isValid = false
        }

        if(companyName.isBlank()){
            state.companyNameError = R.string.required_field
            isValid = false
        }

        if(phone.isBlank()){
            state.phoneError = R.string.required_field
            isValid = false
        }

        if(isCredentialsRegister()) {
            if (password.isBlank()) {
                state.passwordError = R.string.required_field
                isValid = false
            }

            if (passwordRepeat.isBlank()) {
                state.passwordRepeatError = R.string.required_field
                isValid = false
            }

            if (!isMatchPasswords(password, passwordRepeat)) {
                state.passwordRepeatError = R.string.no_match_passwords
                isValid = false
            }
        }

        state.isDataValid = isValid
        _registerUserFormState.value = state
    }

    private fun isMatchPasswords(password: String, passwordRepeat: String): Boolean {
        return password == passwordRepeat
    }
}