package com.mati_tech.dca.dcanew.viewmodel
import android.util.Log
import androidx.lifecycle.*
import com.mati_tech.dca.dcanew.remoteApi.models.User
import com.mati_tech.dca.dcanew.remoteApi.models.UserLoginRequest
import com.mati_tech.dca.dcanew.remoteApi.repos.UserRepos
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepos) : ViewModel() {

    private val _allUsers = MutableLiveData<List<User>?>()
    val allUsers: LiveData<List<User>?> get() = _allUsers

    private val _userDetails = MutableLiveData<User?>()
    val userDetails: LiveData<User?> get() = _userDetails

    private val _isUserRegistered = MutableLiveData<Boolean>()
    val isUserRegistered: LiveData<Boolean> get() = _isUserRegistered

    private val _loginToken = MutableLiveData<String?>()
    val loginToken: LiveData<String?> get() = _loginToken

    private val _isUserDeleted = MutableLiveData<Boolean>()
    val isUserDeleted: LiveData<Boolean> get() = _isUserDeleted

    private val _networkError = MutableLiveData<Boolean>()
    val networkError: LiveData<Boolean> get() = _networkError

    private val _userByEmail = MutableLiveData<User?>()
    val userByEmail: LiveData<User?> = _userByEmail


    // 🔐 Register a new user
    fun registerUser(user: User) {
        viewModelScope.launch {
            try {
                val response = repository.register(user)

                when (response.code()) {
                    201 -> {
                        // Registered successfully
                        val apiResponse = response.body()
                        Log.d("UserViewModel", "Success: ${apiResponse?.message}")
                        _isUserRegistered.postValue(true)
                    }

                    409 -> {
                        // Conflict - User already exists
                        val apiResponse = response.body()
                        Log.e("UserViewModel", "Conflict: ${apiResponse?.message}")
                        _isUserRegistered.postValue(false)
                    }

                    else -> {
                        val errorBody = response.errorBody()?.string()
                        Log.e(
                            "UserViewModel",
                            "Registration failed: ${response.code()} - ${response.message()} - $errorBody"
                        )
                        _isUserRegistered.postValue(false)
                    }
                }

            } catch (e: Exception) {
                Log.e("UserViewModel", "Registration error: ${e.message}")
                _isUserRegistered.postValue(false)
            }
        }
    }


//    fun getUserByEmail(token: String, email: String) {
//        viewModelScope.launch {
//            try {
//                val response = repository.getUserByEmail("Bearer $token", email)
//                if (response.isSuccessful) {
//                    _userByEmail.postValue(response.body())
//                } else {
//                    Log.e("UserViewModel", "Get user failed: ${response.code()} - ${response.message()}")
//                    _userByEmail.postValue(null)
//                }
//            } catch (e: Exception) {
//                Log.e("UserViewModel", "Error: ${e.message}")
//                _userByEmail.postValue(null)
//            }
//        }
//    }

    // 🔐 Login user and store token
    fun loginUser(request: UserLoginRequest) {
        viewModelScope.launch {
            try {
                val response = repository.login(request)
                if (response.isSuccessful) {
                    val token = response.body()?.get("token")
                    _loginToken.postValue(token)
                    _networkError.postValue(false)
                } else {
                    Log.e("UserViewModel", "Login failed: ${response.code()}")
                    _loginToken.postValue(null)
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Login exception: ${e.message}")
                _networkError.postValue(true)
                _loginToken.postValue(null)
            }
        }
    }

    // 🔍 Get all users (admin only)
    fun fetchAllUsers(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllUsers(token)
                if (response.isSuccessful) {
                    _allUsers.postValue(response.body())
                    _networkError.postValue(false)
                } else {
                    Log.e("UserViewModel", "Fetch users failed: ${response.code()}")
                    _networkError.postValue(true)
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Fetch users exception: ${e.message}")
                _networkError.postValue(true)
            }
        }
    }

    // 🔍 Get user by ID
    fun fetchUserById(token: String, userId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getUserById(token, userId)
                _userDetails.postValue(response.body())
            } catch (e: Exception) {
                Log.e("UserViewModel", "Get user by ID error: ${e.message}")
            }
        }
    }

    // 🔍 Get user by Email
    fun fetchUserByEmail(token: String, email: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUserByEmail(token, email)
                _userDetails.postValue(response.body())
                Log.d("UserViewModel", "User response body: ${response.body()}")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Get user by email error: ${e.message}")
            }
        }
    }

    // ✏️ Update user
    fun updateUser(token: String, userId: Int, user: User) {
        viewModelScope.launch {
            try {
                val response = repository.updateUser(token, userId, user)
                if (!response.isSuccessful) {
                    Log.e("UserViewModel", "Update failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Update error: ${e.message}")
            }
        }
    }

    // ❌ Delete user
    fun deleteUser(token: String, userId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.deleteUser(token, userId)
                _isUserDeleted.postValue(response.isSuccessful)
                if (!response.isSuccessful) {
                    Log.e("UserViewModel", "Delete failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Delete error: ${e.message}")
                _isUserDeleted.postValue(false)
            }
        }
    }

    // 🧹 Clear all user list
    fun clearUsers() {
        _allUsers.postValue(emptyList())
    }
}
