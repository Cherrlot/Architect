package com.cherrlot.lib_common.mmkv

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @des    mmkv工具类
 */
class MMKVUtil <T>(var default: T) : ReadWriteProperty<Any?, T> {
    companion object {
        private const val MMKV_NAME_USER = "User"

        fun put(key: String, value: Any?) {
            val mmkv = MMKV.mmkvWithID(MMKV_NAME_USER)
            when (value) {
                is String -> mmkv?.encode(key, value)
                is Float -> mmkv?.encode(key, value)
                is Boolean -> mmkv?.encode(key, value)
                is Int -> mmkv?.encode(key, value)
                is Long -> mmkv?.encode(key, value)
                is Double -> mmkv?.encode(key, value)
                is ByteArray -> mmkv?.encode(key, value)
                is Parcelable -> mmkv?.encode(key, value)
                is Nothing -> return
            }
        }

        fun getString(key: String): String? = getString(key, "")
        fun getString(key: String, defaultValue: String): String? {
            val mmkv = MMKV.mmkvWithID(MMKV_NAME_USER)
            return mmkv?.decodeString(key, defaultValue)
        }

//        fun getParcelable(key: String): Parcelable? = getParcelable(key, null)
        fun <T : Parcelable> getParcelable(key: String, defaultValue: T): T? {
            val mmkv = MMKV.mmkvWithID(MMKV_NAME_USER)
            return mmkv?.decodeParcelable(key, defaultValue.javaClass)
        }

        fun getBoolean(key: String): Boolean? = getBoolean(key, false)
        fun getBoolean(key: String, defaultValue: Boolean): Boolean? {
            val mmkv = MMKV.mmkvWithID(MMKV_NAME_USER)
            return mmkv?.decodeBool(key, defaultValue)
        }

        fun getInt(key: String): Int? = getInt(key, 0)
        fun getInt(key: String, defaultValue: Int): Int? {
            val mmkv = MMKV.mmkvWithID(MMKV_NAME_USER)
            return mmkv?.decodeInt(key, defaultValue)
        }

        fun getFloat(key: String): Float? = getFloat(key, 0f)
        fun getFloat(key: String, defaultValue: Float): Float? {
            val mmkv = MMKV.mmkvWithID(MMKV_NAME_USER)
            return mmkv?.decodeFloat(key, defaultValue)
        }

        fun getLong(key: String): Long? = getLong(key, 0)
        fun getLong(key: String, defaultValue: Long): Long? {
            val mmkv = MMKV.mmkvWithID(MMKV_NAME_USER)
            return mmkv?.decodeLong(key, defaultValue)
        }

        fun getDouble(key: String): Double? = getDouble(key, 0.0)
        fun getDouble(key: String, defaultValue: Double): Double? {
            val mmkv = MMKV.mmkvWithID(MMKV_NAME_USER)
            return mmkv?.decodeDouble(key, defaultValue)
        }

        fun getByteArray(key: String): ByteArray? = getByteArray(key, byteArrayOf())
        fun getByteArray(key: String, defaultValue: ByteArray): ByteArray? {
            val mmkv = MMKV.mmkvWithID(MMKV_NAME_USER)
            return mmkv?.decodeBytes(key, defaultValue)
        }


        fun remove(key: String) {
            val mmkv = MMKV.mmkvWithID(MMKV_NAME_USER)
            mmkv?.removeValueForKey(key)
        }

        fun removeKeys(vararg key: String) {
            val mmkv = MMKV.mmkvWithID(MMKV_NAME_USER)
            mmkv?.removeValuesForKeys(key)
        }

        fun clear() {
            MMKV.mmkvWithID(MMKV_NAME_USER)?.clearAll()
        }


        private fun <U> getProperty(key: String, default: U) : U {
            val result: Any? = when(default) {
                is String -> getString(key, default)
                is Float -> getFloat(key, default)
                is Boolean -> getBoolean(key, default)
                is Int -> getInt(key, default)
                is Long -> getLong(key, default)
                is Double -> getDouble(key, default)
                is ByteArray -> getByteArray(key, default)
                is Parcelable -> getParcelable(key, default)
                else -> throw IllegalArgumentException("type is null")
            }
            return result as U
        }
    }

    var mValue: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        mValue = getProperty(property.name, default)
        return mValue!!
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        put(property.name, value)
        mValue = value
    }


}