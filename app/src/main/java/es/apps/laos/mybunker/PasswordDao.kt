package es.apps.laos.mybunker

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PasswordDao {

    @Insert
    fun insertPassword(passwordEntity: PasswordEntity)

    @Insert
    fun insertAll(vararg passwordEntries: PasswordEntity)

    @Update
    fun updateUsers(vararg passwordEntries: PasswordEntity)

    @Delete
    fun delete(passwordEntity: PasswordEntity)

    @Query("SELECT * FROM password_table")
    fun getAll(): List<PasswordEntity>

    @Query("SELECT * FROM password_table WHERE id=(:id)")
    fun getPasswordById(id: Int): PasswordEntity
}