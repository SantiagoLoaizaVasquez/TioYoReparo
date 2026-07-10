package com.t2.appaws14753.`data`.local.dao

import androidx.room3.EntityDeleteOrUpdateAdapter
import androidx.room3.EntityInsertAdapter
import androidx.room3.RoomDatabase
import androidx.room3.util.getColumnIndexOrThrow
import androidx.room3.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.t2.appaws14753.`data`.local.entity.ClienteEntity
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room3.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL", "MemberExtensionConflict"])
internal class ClienteDao_Impl(
  __db: RoomDatabase,
) : ClienteDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfClienteEntity: EntityInsertAdapter<ClienteEntity>

  private val __updateAdapterOfClienteEntity: EntityDeleteOrUpdateAdapter<ClienteEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfClienteEntity = object : EntityInsertAdapter<ClienteEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `clientes` (`id`,`nombre`,`email`,`telefono`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ClienteEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.nombre)
        statement.bindText(3, entity.email)
        statement.bindText(4, entity.telefono)
      }
    }
    this.__updateAdapterOfClienteEntity = object : EntityDeleteOrUpdateAdapter<ClienteEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `clientes` SET `id` = ?,`nombre` = ?,`email` = ?,`telefono` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: ClienteEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.nombre)
        statement.bindText(3, entity.email)
        statement.bindText(4, entity.telefono)
        statement.bindText(5, entity.id)
      }
    }
  }

  public override suspend fun insert(entity: ClienteEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfClienteEntity.insert(_connection, entity)
  }

  public override suspend fun update(entity: ClienteEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfClienteEntity.handle(_connection, entity)
  }

  public override suspend fun getAll(): List<ClienteEntity> {
    val _sql: String = "SELECT * FROM clientes"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfNombre: Int = getColumnIndexOrThrow(_stmt, "nombre")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfTelefono: Int = getColumnIndexOrThrow(_stmt, "telefono")
        val _result: MutableList<ClienteEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ClienteEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpNombre: String
          _tmpNombre = _stmt.getText(_columnIndexOfNombre)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpTelefono: String
          _tmpTelefono = _stmt.getText(_columnIndexOfTelefono)
          _item = ClienteEntity(_tmpId,_tmpNombre,_tmpEmail,_tmpTelefono)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: String): ClienteEntity? {
    val _sql: String = "SELECT * FROM clientes WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfNombre: Int = getColumnIndexOrThrow(_stmt, "nombre")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfTelefono: Int = getColumnIndexOrThrow(_stmt, "telefono")
        val _result: ClienteEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpNombre: String
          _tmpNombre = _stmt.getText(_columnIndexOfNombre)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpTelefono: String
          _tmpTelefono = _stmt.getText(_columnIndexOfTelefono)
          _result = ClienteEntity(_tmpId,_tmpNombre,_tmpEmail,_tmpTelefono)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun delete(id: String) {
    val _sql: String = "DELETE FROM clientes WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredColumnConverters(): List<KClass<*>> = emptyList()

    public fun getRequiredDaoReturnTypeConverters(): List<KClass<*>> = emptyList()
  }
}
