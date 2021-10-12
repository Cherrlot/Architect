package com.cherrlot.lib_common.util

import java.security.MessageDigest

/**
 * description:
 */
class Md5Utils {

    companion object {

        fun Md5(string: String): String {
            val md5: MessageDigest?
            try {
                md5 = MessageDigest.getInstance("MD5")
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }

            val charArray = string.toCharArray()
            val byteArray = ByteArray(charArray.size)

            for (i in charArray.indices) {
                byteArray[i] = charArray[i].toByte()
            }
            val md5Bytes = md5!!.digest(byteArray)

            val hexValue = StringBuffer()
            for (i in md5Bytes.indices) {
                val bytes = md5Bytes[i].toInt() and 0xff
                if (bytes < 16) {
                    hexValue.append("0")
                }
                hexValue.append(Integer.toHexString(bytes))
            }
            return hexValue.toString()
        }
    }
}