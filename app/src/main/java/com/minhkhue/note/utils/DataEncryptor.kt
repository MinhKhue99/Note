package com.minhkhue.note.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.security.Key
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.SecureRandom
import java.util.Calendar
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.security.auth.x500.X500Principal

interface DataEncryptor {
	fun encryptString(src: String): String
	fun decryptString(src: String): String
}

/**
 * Modern encryption/decryption for Android API >= 23
 * (Android M and above)
 */

class DataEncryptorModern : DataEncryptor {
	
	companion object {
		private const val KEY_ALIAS = "KEY_ALIAS"
		private const val PROVIDER_ANDROID_KEY_STORE = "AndroidKeyStore"
		private const val CIPHER_ALGORITHM = "AES/GCM/NoPadding"
		private const val TAG_LENGTH = 128
		
		private const val TAG = "DataEncryptorModern"
	}
	
	private var INIT_VECTOR = "abcdefghijkl"
	private var cipher: Cipher = Cipher.getInstance(CIPHER_ALGORITHM)
	
	private val keyStore: KeyStore = KeyStore.getInstance(PROVIDER_ANDROID_KEY_STORE)
	private val keyGenerator: KeyGenerator =
		KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, PROVIDER_ANDROID_KEY_STORE)
	
	@Synchronized
	override fun encryptString(src: String): String {
		return try {
			initCipher(Cipher.ENCRYPT_MODE)
			val encryptedBytes = cipher.doFinal(src.toByteArray())
			Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
		} catch (ex: Exception) {
			Log.e(TAG, "encrypt(): $ex")
			""
		}
	}
	
	@Synchronized
	override fun decryptString(src: String): String {
		return try {
			initCipher(Cipher.DECRYPT_MODE)
			val bytes = Base64.decode(src, Base64.NO_WRAP)
			return String(cipher.doFinal(bytes))
		} catch (ex: Exception) {
			Log.e(TAG, "decrypt(): $ex")
			""
		}
	}
	
	@TargetApi(Build.VERSION_CODES.M)
	private fun initCipher(mode: Int) {
		try {
			cipher.init(mode, getOrCreateKey(), getGcmSpec())
		} catch (invalidKeyException: KeyPermanentlyInvalidatedException) {
			Log.d(TAG, "initCipher(): Invalid Key: $invalidKeyException")
			deleteInvalidKey()
		} catch (ex: Exception) {
			Log.e(TAG, "initCipher(): $ex")
		}
	}
	
	private fun getOrCreateKey(): SecretKey {
		keyStore.load(null)
		if (!keyStore.containsAlias(KEY_ALIAS)) generateKey()
		return getExistingKey()
	}
	
	@TargetApi(Build.VERSION_CODES.M)
	private fun generateKey() {
		keyGenerator.init(getKeyGenParams())
		keyGenerator.generateKey()
	}
	
	private fun getExistingKey(): SecretKey {
		keyStore.load(null)
		return (keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
	}
	
	private fun deleteInvalidKey() {
		keyStore.load(null)
		keyStore.deleteEntry(KEY_ALIAS)
	}
	
	@TargetApi(Build.VERSION_CODES.M)
	private fun getKeyGenParams(): KeyGenParameterSpec {
		return KeyGenParameterSpec.Builder(
			KEY_ALIAS,
			KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
		)
			.setBlockModes(KeyProperties.BLOCK_MODE_GCM)
			.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
			.setRandomizedEncryptionRequired(false)
			.build()
	}
	
	private fun getGcmSpec(): GCMParameterSpec {
		return GCMParameterSpec(TAG_LENGTH, INIT_VECTOR.toByteArray())
	}
	
}