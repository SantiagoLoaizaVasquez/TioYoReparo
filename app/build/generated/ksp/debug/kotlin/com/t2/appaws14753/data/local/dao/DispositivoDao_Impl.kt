package com.t2.appaws14753.`data`.local.dao

import androidx.room3.EntityDeleteOrUpdateAdapter
import androidx.room3.EntityInsertAdapter
import androidx.room3.RoomDatabase
import androidx.room3.util.getColumnIndexOrThrow
import androidx.room3.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.t2.appaws14753.`data`.local.entity.DispositivoEntity
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
internal class DispositivoDao_Impl(
  __db: RoomDatabase,
) : DispositivoDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfDispositivoEntity: EntityInsertAdapter<DispositivoEntity>

  private val __updateAdapterOfDispositivoEntity: EntityDeleteOrUpdateAdapter<DispositivoEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfDispositivoEntity = object : EntityInsertAdapter<DispositivoEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `dispositivo` (`dispositivoId`,`clienteId`,`marca`,`modelo`,`numeroSerie`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DispositivoEntity) {
        statement.bindText(1, entity.dispositivoId)
        statement.bindText(2, entity.clienteId)
        statement.bindText(3, entity.marca)
        statement.bindText(4, entity.modelo)
        statement.bindText(5, entity.numeroSerie)
      }
    }
    this.__updateAdapterOfDispositivoEntity = object : EntityDeleteOrUpdateAdapter<DispositivoEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `dispositivo` SET `dispositivoId` = ?,`clienteId` = ?,`marca` = ?,`modelo` = ?,`numeroSerie` = ? WHERE `dispositivoId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: DispositivoEntity) {
        statement.bindText(1, entity.dispositivoId)
        statement.bindText(2, entity.clienteId)
        statement.bindText(3, entity.marca)
        statement.bindText(4, entity.modelo)
        statement.bindText(5, entity.numeroSerie)
        statement.bindText(6, entity.dispositivoId)
      }
    }
  }

  public override suspend fun insert(entity: DispositivoEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfDispositivoEntity.insert(_connection, entity)
  }

  public override suspend fun update(entity: DispositivoEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfDispositivoEntity.handle(_connection, entity)
  }

  public override suspend fun getAll(): List<DispositivoEntity> {
    val _sql: String = "SELECT * FROM dispositivo"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfDispositivoId: Int = getColumnIndexOrThrow(_stmt, "dispositivoId")
        val _columnIndexOfClienteId: Int = getColumnIndexOrThrow(_stmt, "clienteId")
        val _columnIndexOfMarca: Int = getColumnIndexOrThrow(_stmt, "marca")
        val _columnIndexOfModelo: Int = getColumnIndexOrThrow(_stmt, "modelo")
        val _columnIndexOfNumeroSerie: Int = getColumnIndexOrThrow(_stmt, "numeroSerie")
        val _result: MutableList<DispositivoEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: DispositivoEntity
          val _tmpDispositivoId: String
          _tmpDispositivoId = _stmt.getText(_columnIndexOfDispositivoId)
          val _tmpClienteId: String
          _tmpClienteId = _stmt.getText(_columnIndexOfClienteId)
          val _tmpMarca: String
          _tmpMarca = _stmt.getText(_columnIndexOfMarca)
          val _tmpModelo: String
          _tmpModelo = _stmt.getText(_columnIndexOfModelo)
          val _tmpNumeroSerie: String
          _tmpNumeroSerie = _stmt.getText(_columnIndexOfNumeroSerie)
          _item = DispositivoEntity(_tmpDispositivoId,_tmpClienteId,_tmpMarca,_tmpModelo,_tmpNumeroSerie)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: String): DispositivoEntity? {
    val _sql: String = "SELECT * FROM dispositivo WHERE dispositivoId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfDispositivoId: Int = getColumnIndexOrThrow(_stmt, "dispositivoId")
        val _columnIndexOfClienteId: Int = getColumnIndexOrThrow(_stmt, "clienteId")
        val _columnIndexOfMarca: Int = getColumnIndexOrThrow(_stmt, "marca")
        val _columnIndexOfModelo: Int = getColumnIndexOrThrow(_stmt, "modelo")
        val _columnIndexOfNumeroSerie: Int = getColumnIndexOrThrow(_stmt, "numeroSerie")
        val _result: DispositivoEntity?
        if (_stmt.step()) {
          val _tmpDispositivoId: String
          _tmpDispositivoId = _stmt.getText(_columnIndexOfDispositivoId)
          val _tmpClienteId: String
          _tmpClienteId = _stmt.getText(_columnIndexOfClienteId)
          val _tmpMarca: String
          _tmpMarca = _stmt.getText(_columnIndexOfMarca)
          val _tmpModelo: String
          _tmpModelo = _stmt.getText(_columnIndexOfModelo)
          val _tmpNumeroSerie: String
          _tmpNumeroSerie = _stmt.getText(_columnIndexOfNumeroSerie)
          _result = DispositivoEntity(_tmpDispositivoId,_tmpClienteId,_tmpMarca,_tmpModelo,_tmpNumeroSerie)
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
    val _sql: String = "DELETE FROM dispositivo WHERE dispositivoId = ?"
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
