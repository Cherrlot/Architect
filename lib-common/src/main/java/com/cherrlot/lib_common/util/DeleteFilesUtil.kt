package com.cherrlot.lib_common.util

import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * 删除文件，文件夹
 */
class DeleteFilesUtil {

    companion object {
        /**
         * 保留一定数目的文件，其他删掉
         * @param path String
         * @param retainCount Int
         */
        fun deleteFiles(path: String, retainCount: Int) {
            val list = getFileSort(path)
            if (list.size <= retainCount)
                return
            for (i in list.indices) {
                if (i >= retainCount) {
                    list[i].delete()
                }
            }
        }

        /**
         * 保留一定数目的文件，其他删掉
         * @param path String
         * @param retainCount Int
         */
        fun deleteDirectoryFiles(path: String, retainCount: Int) {
            val list = getDirectorySort(path)
            if (list.size <= retainCount)
                return
            for (i in list.indices) {
                if (i >= retainCount) {
                    delete(list[i].absolutePath)
                }
            }
        }

        /**
         * 获取目录下所有文件(按时间排序)
         *
         * @param path
         * @return
         */
        fun getDirectorySort(path: String): List<File> {
            val realFile = File(path)
            val list = realFile.listFiles()
            return if (list.isNullOrEmpty()) {
                mutableListOf()
            } else {
                list.sortWith { o1, o2 -> o2.name.compareTo(o1.name) }
                list.toMutableList()
            }
        }

        /**
         * 获取目录下所有文件(按时间排序)
         *
         * @param path
         * @return
         */
        fun getFileSort(path: String): List<File> {
            val list = getFiles(path, ArrayList())
            if (list.isNotEmpty()) {
                Collections.sort(
                    list
                ) { o1, o2 -> o2.name.compareTo(o1.name) }
            }
            return list
        }

        /**
         *
         * 获取目录下所有文件
         *
         * @param realpath
         * @param files
         * @return
         */
        fun getFiles(realpath: String, files: MutableList<File>): List<File> {
            val realFile = File(realpath)
            if (realFile.isDirectory) {
                val subfiles = realFile.listFiles()
                for (file in subfiles) {
                    if (file.isDirectory) {
                        getFiles(file.absolutePath, files)
                    } else {
                        files.add(file)
                    }
                }
            }
            return files
        }

        /**
         * 删除文件，可以是文件或文件夹
         *
         * @param fileName
         *            要删除的文件名
         * @return 删除成功返回true，否则返回false
         */
        fun delete(fileName: String): Boolean {
            val file = File(fileName)
            return if (!file.exists()) {
                System.out.println("删除文件失败:" + fileName + "不存在！")
                false
            } else {
                if (file.isFile()) deleteFile(fileName) else deleteDirectory(fileName)
            }
        }

        /**
         * 删除单个文件
         *
         * @param fileName
         *            要删除的文件的文件名
         * @return 单个文件删除成功返回true，否则返回false
         */
        fun deleteFile(fileName: String): Boolean {
            val file = File(fileName)
            // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
            // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
            return if (file.exists() && file.isFile) {
                if (file.delete()) {
                    System.out.println("删除单个文件" + fileName + "成功！")
                    true
                } else {
                    System.out.println("删除单个文件" + fileName + "失败！")
                    false
                }
            } else {
                System.out.println("删除单个文件失败：" + fileName + "不存在！")
                false
            }
        }

        /**
         * 删除目录及目录下的文件
         *
         * @param dir
         *            要删除的目录的文件路径
         * @return 目录删除成功返回true，否则返回false
         */
        fun deleteDirectory(dir: String): Boolean {
            var dirPath: String = dir
            // 如果dir不以文件分隔符结尾，自动添加文件分隔符
            // 如果dir不以文件分隔符结尾，自动添加文件分隔符
            if (!dir.endsWith(File.separator)) dirPath = dir + File.separator
            val dirFile = File(dirPath)
            // 如果dir对应的文件不存在，或者不是一个目录，则退出
            // 如果dir对应的文件不存在，或者不是一个目录，则退出
            if (!dirFile.exists() || !dirFile.isDirectory) {
                System.out.println("删除目录失败：" + dir + "不存在！")
                return false
            }
            var flag = true
            // 删除文件夹中的所有文件包括子目录
            // 删除文件夹中的所有文件包括子目录
            val files = dirFile.listFiles()
            for (i in files.indices) {
                // 删除子文件
                if (files[i].isFile) {
                    flag = deleteFile(files[i].absolutePath)
                    if (!flag) break
                } else if (files[i].isDirectory) {
                    flag = deleteDirectory(
                        files[i]
                            .absolutePath
                    )
                    if (!flag) break
                }
            }
            if (!flag) {
                println("删除目录失败！")
                return false
            }
            // 删除当前目录
            // 删除当前目录
            return if (dirFile.delete()) {
                System.out.println("删除目录" + dir + "成功！")
                true
            } else {
                false
            }
        }
    }
}
