package com.t2.appaws14753.`data`.local.dao

import androidx.room3.EntityDeleteOrUpdateAdapter
import androidx.room3.EntityInsertAdapter
import androidx.room3.RoomDatabase
import androidx.room3.util.getColumnIndexOrThrow
import androidx.room3.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.t2.appaws14753.`data`.local.entity.OrdenEntity
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room3.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL", "MemberExtensionConflict"])
internal class OrdenDao_Impl(
  __db: RoomDatabase,
) : OrdenDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfOrdenEntity: EntityInsertAdapter<OrdenEntity>

  private val __updateAdapterOfOrdenEntity: EntityDeleteOrUpdateAdapter<OrdenEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfOrdenEntity = object : EntityInsertAdapter<OrdenEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `orden` (`ordenId`,`dispositivoId`,`clienteId`,`tecnicoId`,`tecnicoNombre`,`estado`,`prioridad`,`fechaIngreso`,`fechaEntrega`,`detalleDiagnostico`,`totalCobrado`,`servicios`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: OrdenEntity) {
        statement.bindText(1, entity.ordenId)
        statement.bindText(2, entity.dispositivoId)
        statement.bindText(3, entity.clienteId)
        statement.bindText(4, entity.tecnicoId)
        statement.bindText(5, entity.tecnicoNombre)
        statement.bindText(6, entity.estado)
        statement.bindText(7, entity.prioridad)
        statement.bindLong(8, entity.fechaIngreso)
        val _tmpFechaEntrega: Long? = entity.fechaEntrega
        if (_tmpFechaEntrega == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmpFechaEntrega)
        }
        statement.bindText(10, entity.detalleDiagnostico)
        statement.bindDouble(11, entity.totalCobrado)
        statement.bindText(12, entity.servicios)
      }
    }
    this.__updateAdapterOfOrdenEntity = object : EntityDeleteOrUpdateAdapter<OrdenEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `orden` SET `ordenId` = ?,`dispositivoId` = ?,`clienteId` = ?,`tecnicoId` = ?,`tecnicoNombre` = ?,`estado` = ?,`prioridad` = ?,`fechaIngreso` = ?,`fechaEntrega` = ?,`detalleDiagnostico` = ?,`totalCobrado` = ?,`servicios` = ? WHERE `ordenId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: OrdenEntity) {
        statement.bindText(1, entity.ordenId)
        statement.bindText(2, entity.dispositivoId)
        statement.bindText(3, entity.clienteId)
        statement.bindText(4, entity.tecnicoId)
        statement.bindText(5, entity.tecnicoNombre)
        statement.bindText(6, entity.estado)
        statement.bindText(7, entity.prioridad)
        statement.bindLong(8, entity.fechaIngreso)
        val _tmpFechaEntrega: Long? = entity.fechaEntrega
        if (_tmpFechaEntrega == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmpFechaEntrega)
        }
        statement.bindText(10, entity.detalleDiagnostico)
        statement.bindDouble(11, entity.totalCobrado)
        statement.bindText(12, entity.servicios)
        statement.bindText(13, entity.ordenId)
      }
    }
  }

  public override suspend fun insert(entity: OrdenEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfOrdenEntity.insert(_connection, entity)
  }

  public override suspend fun update(entity: OrdenEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfOrdenEntity.handle(_connection, entity)
  }

  public override suspend fun getAll(): List<OrdenEntity> {
    val _sql: String = "SELECT * FROM orden"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfOrdenId: Int = getColumnIndexOrThrow(_stmt, "ordenId")
        val _columnIndexOfDispositivoId: Int = getColumnIndexOrThrow(_stmt, "dispositivoId")
        val _columnIndexOfClienteId: Int = getColumnIndexOrThrow(_stmt, "clienteId")
        val _columnIndexOfTecnicoId: Int = getColumnIndexOrThrow(_stmt, "tecnicoId")
        val _columnIndexOfTecnicoNombre: Int = getColumnIndexOrThrow(_stmt, "tecnicoNombre")
        val _columnIndexOfEstado: Int = getColumnIndexOrThrow(_stmt, "estado")
        val _columnIndexOfPrioridad: Int = getColumnIndexOrThrow(_stmt, "prioridad")
        val _columnIndexOfFechaIngreso: Int = getColumnIndexOrThrow(_stmt, "fechaIngreso")
        val _columnIndexOfFechaEntrega: Int = getColumnIndexOrThrow(_stmt, "fechaEntrega")
        val _columnIndexOfDetalleDiagnostico: Int = getColumnIndexOrThrow(_stmt, "detalleDiagnostico")
        val _columnIndexOfTotalCobrado: Int = getColumnIndexOrThrow(_stmt, "totalCobrado")
        val _columnIndexOfServicios: Int = getColumnIndexOrThrow(_stmt, "servicios")
        val _result: MutableList<OrdenEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: OrdenEntity
          val _tmpOrdenId: String
          _tmpOrdenId = _stmt.getText(_columnIndexOfOrdenId)
          val _tmpDispositivoId: String
          _tmpDispositivoId = _stmt.getText(_columnIndexOfDispositivoId)
          val _tmpClienteId: String
          _tmpClienteId = _stmt.getText(_columnIndexOfClienteId)
          val _tmpTecnicoId: String
          _tmpTecnicoId = _stmt.getText(_columnIndexOfTecnicoId)
          val _tmpTecnicoNombre: String
          _tmpTecnicoNombre = _stmt.getText(_columnIndexOfTecnicoNombre)
          val _tmpEstado: String
          _tmpEstado = _stmt.getText(_columnIndexOfEstado)
          val _tmpPrioridad: String
          _tmpPrioridad = _stmt.getText(_columnIndexOfPrioridad)
          val _tmpFechaIngreso: Long
          _tmpFechaIngreso = _stmt.getLong(_columnIndexOfFechaIngreso)
          val _tmpFechaEntrega: Long?
          if (_stmt.isNull(_columnIndexOfFechaEntrega)) {
            _tmpFechaEntrega = null
          } else {
            _tmpFechaEntrega = _stmt.getLong(_columnIndexOfFechaEntrega)
          }
          val _tmpDetalleDiagnostico: String
          _tmpDetalleDiagnostico = _stmt.getText(_columnIndexOfDetalleDiagnostico)
          val _tmpTotalCobrado: Double
          _tmpTotalCobrado = _stmt.getDouble(_columnIndexOfTotalCobrado)
          val _tmpServicios: String
          _tmpServicios = _stmt.getText(_columnIndexOfServicios)
          _item = OrdenEntity(_tmpOrdenId,_tmpDispositivoId,_tmpClienteId,_tmpTecnicoId,_tmpTecnicoNombre,_tmpEstado,_tmpPrioridad,_tmpFechaIngreso,_tmpFechaEntrega,_tmpDetalleDiagnostico,_tmpTotalCobrado,_tmpServicios)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(ordenId: String): OrdenEntity? {
    val _sql: String = "SELECT * FROM orden WHERE ordenId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, ordenId)
        val _columnIndexOfOrdenId: Int = getColumnIndexOrThrow(_stmt, "ordenId")
        val _columnIndexOfDispositivoId: Int = getColumnIndexOrThrow(_stmt, "dispositivoId")
        val _columnIndexOfClienteId: Int = getColumnIndexOrThrow(_stmt, "clienteId")
        val _columnIndexOfTecnicoId: Int = getColumnIndexOrThrow(_stmt, "tecnicoId")
        val _columnIndexOfTecnicoNombre: Int = getColumnIndexOrThrow(_stmt, "tecnicoNombre")
        val _columnIndexOfEstado: Int = getColumnIndexOrThrow(_stmt, "estado")
        val _columnIndexOfPrioridad: Int = getColumnIndexOrThrow(_stmt, "prioridad")
        val _columnIndexOfFechaIngreso: Int = getColumnIndexOrThrow(_stmt, "fechaIngreso")
        val _columnIndexOfFechaEntrega: Int = getColumnIndexOrThrow(_stmt, "fechaEntrega")
        val _columnIndexOfDetalleDiagnostico: Int = getColumnIndexOrThrow(_stmt, "detalleDiagnostico")
        val _columnIndexOfTotalCobrado: Int = getColumnIndexOrThrow(_stmt, "totalCobrado")
        val _columnIndexOfServicios: Int = getColumnIndexOrThrow(_stmt, "servicios")
        val _result: OrdenEntity?
        if (_stmt.step()) {
          val _tmpOrdenId: String
          _tmpOrdenId = _stmt.getText(_columnIndexOfOrdenId)
          val _tmpDispositivoId: String
          _tmpDispositivoId = _stmt.getText(_columnIndexOfDispositivoId)
          val _tmpClienteId: String
          _tmpClienteId = _stmt.getText(_columnIndexOfClienteId)
          val _tmpTecnicoId: String
          _tmpTecnicoId = _stmt.getText(_columnIndexOfTecnicoId)
          val _tmpTecnicoNombre: String
          _tmpTecnicoNombre = _stmt.getText(_columnIndexOfTecnicoNombre)
          val _tmpEstado: String
          _tmpEstado = _stmt.getText(_columnIndexOfEstado)
          val _tmpPrioridad: String
          _tmpPrioridad = _stmt.getText(_columnIndexOfPrioridad)
          val _tmpFechaIngreso: Long
          _tmpFechaIngreso = _stmt.getLong(_columnIndexOfFechaIngreso)
          val _tmpFechaEntrega: Long?
          if (_stmt.isNull(_columnIndexOfFechaEntrega)) {
            _tmpFechaEntrega = null
          } else {
            _tmpFechaEntrega = _stmt.getLong(_columnIndexOfFechaEntrega)
          }
          val _tmpDetalleDiagnostico: String
          _tmpDetalleDiagnostico = _stmt.getText(_columnIndexOfDetalleDiagnostico)
          val _tmpTotalCobrado: Double
          _tmpTotalCobrado = _stmt.getDouble(_columnIndexOfTotalCobrado)
          val _tmpServicios: String
          _tmpServicios = _stmt.getText(_columnIndexOfServicios)
          _result = OrdenEntity(_tmpOrdenId,_tmpDispositivoId,_tmpClienteId,_tmpTecnicoId,_tmpTecnicoNombre,_tmpEstado,_tmpPrioridad,_tmpFechaIngreso,_tmpFechaEntrega,_tmpDetalleDiagnostico,_tmpTotalCobrado,_tmpServicios)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun delete(ordenId: String) {
    val _sql: String = "DELETE FROM orden WHERE ordenId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, ordenId)
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
