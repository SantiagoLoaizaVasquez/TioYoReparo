package com.t2.appaws14753.`data`.local.dao

import androidx.room3.EntityDeleteOrUpdateAdapter
import androidx.room3.EntityInsertAdapter
import androidx.room3.RoomDatabase
import androidx.room3.util.getColumnIndexOrThrow
import androidx.room3.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.t2.appaws14753.`data`.local.entity.ServicioEntity
import javax.`annotation`.processing.Generated
import kotlin.Double
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
internal class ServicioDao_Impl(
  __db: RoomDatabase,
) : ServicioDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfServicioEntity: EntityInsertAdapter<ServicioEntity>

  private val __updateAdapterOfServicioEntity: EntityDeleteOrUpdateAdapter<ServicioEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfServicioEntity = object : EntityInsertAdapter<ServicioEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `servicio` (`servicioId`,`nombreServicio`,`precioServicio`) VALUES (?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ServicioEntity) {
        statement.bindText(1, entity.servicioId)
        statement.bindText(2, entity.nombreServicio)
        statement.bindDouble(3, entity.precioServicio)
      }
    }
    this.__updateAdapterOfServicioEntity = object : EntityDeleteOrUpdateAdapter<ServicioEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `servicio` SET `servicioId` = ?,`nombreServicio` = ?,`precioServicio` = ? WHERE `servicioId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: ServicioEntity) {
        statement.bindText(1, entity.servicioId)
        statement.bindText(2, entity.nombreServicio)
        statement.bindDouble(3, entity.precioServicio)
        statement.bindText(4, entity.servicioId)
      }
    }
  }

  public override suspend fun insert(entity: ServicioEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfServicioEntity.insert(_connection, entity)
  }

  public override suspend fun update(entity: ServicioEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfServicioEntity.handle(_connection, entity)
  }

  public override suspend fun getAll(): List<ServicioEntity> {
    val _sql: String = "SELECT * FROM servicio"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfServicioId: Int = getColumnIndexOrThrow(_stmt, "servicioId")
        val _columnIndexOfNombreServicio: Int = getColumnIndexOrThrow(_stmt, "nombreServicio")
        val _columnIndexOfPrecioServicio: Int = getColumnIndexOrThrow(_stmt, "precioServicio")
        val _result: MutableList<ServicioEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ServicioEntity
          val _tmpServicioId: String
          _tmpServicioId = _stmt.getText(_columnIndexOfServicioId)
          val _tmpNombreServicio: String
          _tmpNombreServicio = _stmt.getText(_columnIndexOfNombreServicio)
          val _tmpPrecioServicio: Double
          _tmpPrecioServicio = _stmt.getDouble(_columnIndexOfPrecioServicio)
          _item = ServicioEntity(_tmpServicioId,_tmpNombreServicio,_tmpPrecioServicio)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(servicioId: String): ServicioEntity? {
    val _sql: String = "SELECT * FROM servicio WHERE servicioId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, servicioId)
        val _columnIndexOfServicioId: Int = getColumnIndexOrThrow(_stmt, "servicioId")
        val _columnIndexOfNombreServicio: Int = getColumnIndexOrThrow(_stmt, "nombreServicio")
        val _columnIndexOfPrecioServicio: Int = getColumnIndexOrThrow(_stmt, "precioServicio")
        val _result: ServicioEntity?
        if (_stmt.step()) {
          val _tmpServicioId: String
          _tmpServicioId = _stmt.getText(_columnIndexOfServicioId)
          val _tmpNombreServicio: String
          _tmpNombreServicio = _stmt.getText(_columnIndexOfNombreServicio)
          val _tmpPrecioServicio: Double
          _tmpPrecioServicio = _stmt.getDouble(_columnIndexOfPrecioServicio)
          _result = ServicioEntity(_tmpServicioId,_tmpNombreServicio,_tmpPrecioServicio)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun delete(servicioId: String) {
    val _sql: String = "DELETE FROM servicio WHERE servicioId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, servicioId)
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
