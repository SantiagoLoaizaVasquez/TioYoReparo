package com.t2.appaws14753.`data`.local.dao

import androidx.room3.EntityDeleteOrUpdateAdapter
import androidx.room3.EntityInsertAdapter
import androidx.room3.RoomDatabase
import androidx.room3.util.getColumnIndexOrThrow
import androidx.room3.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.t2.appaws14753.`data`.local.entity.UsuarioEntity
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
internal class UsuarioDao_Impl(
  __db: RoomDatabase,
) : UsuarioDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUsuarioEntity: EntityInsertAdapter<UsuarioEntity>

  private val __updateAdapterOfUsuarioEntity: EntityDeleteOrUpdateAdapter<UsuarioEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfUsuarioEntity = object : EntityInsertAdapter<UsuarioEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `usuario` (`usuarioId`,`rol`,`correo`,`contrasena`,`nombres`,`apellidoPaterno`,`apellidoMaterno`,`especialidad`) VALUES (?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: UsuarioEntity) {
        statement.bindText(1, entity.usuarioId)
        statement.bindText(2, entity.rol)
        statement.bindText(3, entity.correo)
        statement.bindText(4, entity.contrasena)
        statement.bindText(5, entity.nombres)
        statement.bindText(6, entity.apellidoPaterno)
        val _tmpApellidoMaterno: String? = entity.apellidoMaterno
        if (_tmpApellidoMaterno == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpApellidoMaterno)
        }
        val _tmpEspecialidad: String? = entity.especialidad
        if (_tmpEspecialidad == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpEspecialidad)
        }
      }
    }
    this.__updateAdapterOfUsuarioEntity = object : EntityDeleteOrUpdateAdapter<UsuarioEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `usuario` SET `usuarioId` = ?,`rol` = ?,`correo` = ?,`contrasena` = ?,`nombres` = ?,`apellidoPaterno` = ?,`apellidoMaterno` = ?,`especialidad` = ? WHERE `usuarioId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: UsuarioEntity) {
        statement.bindText(1, entity.usuarioId)
        statement.bindText(2, entity.rol)
        statement.bindText(3, entity.correo)
        statement.bindText(4, entity.contrasena)
        statement.bindText(5, entity.nombres)
        statement.bindText(6, entity.apellidoPaterno)
        val _tmpApellidoMaterno: String? = entity.apellidoMaterno
        if (_tmpApellidoMaterno == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpApellidoMaterno)
        }
        val _tmpEspecialidad: String? = entity.especialidad
        if (_tmpEspecialidad == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpEspecialidad)
        }
        statement.bindText(9, entity.usuarioId)
      }
    }
  }

  public override suspend fun insert(entity: UsuarioEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfUsuarioEntity.insert(_connection, entity)
  }

  public override suspend fun update(entity: UsuarioEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfUsuarioEntity.handle(_connection, entity)
  }

  public override suspend fun getAll(): List<UsuarioEntity> {
    val _sql: String = "SELECT * FROM usuario"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfUsuarioId: Int = getColumnIndexOrThrow(_stmt, "usuarioId")
        val _columnIndexOfRol: Int = getColumnIndexOrThrow(_stmt, "rol")
        val _columnIndexOfCorreo: Int = getColumnIndexOrThrow(_stmt, "correo")
        val _columnIndexOfContrasena: Int = getColumnIndexOrThrow(_stmt, "contrasena")
        val _columnIndexOfNombres: Int = getColumnIndexOrThrow(_stmt, "nombres")
        val _columnIndexOfApellidoPaterno: Int = getColumnIndexOrThrow(_stmt, "apellidoPaterno")
        val _columnIndexOfApellidoMaterno: Int = getColumnIndexOrThrow(_stmt, "apellidoMaterno")
        val _columnIndexOfEspecialidad: Int = getColumnIndexOrThrow(_stmt, "especialidad")
        val _result: MutableList<UsuarioEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: UsuarioEntity
          val _tmpUsuarioId: String
          _tmpUsuarioId = _stmt.getText(_columnIndexOfUsuarioId)
          val _tmpRol: String
          _tmpRol = _stmt.getText(_columnIndexOfRol)
          val _tmpCorreo: String
          _tmpCorreo = _stmt.getText(_columnIndexOfCorreo)
          val _tmpContrasena: String
          _tmpContrasena = _stmt.getText(_columnIndexOfContrasena)
          val _tmpNombres: String
          _tmpNombres = _stmt.getText(_columnIndexOfNombres)
          val _tmpApellidoPaterno: String
          _tmpApellidoPaterno = _stmt.getText(_columnIndexOfApellidoPaterno)
          val _tmpApellidoMaterno: String?
          if (_stmt.isNull(_columnIndexOfApellidoMaterno)) {
            _tmpApellidoMaterno = null
          } else {
            _tmpApellidoMaterno = _stmt.getText(_columnIndexOfApellidoMaterno)
          }
          val _tmpEspecialidad: String?
          if (_stmt.isNull(_columnIndexOfEspecialidad)) {
            _tmpEspecialidad = null
          } else {
            _tmpEspecialidad = _stmt.getText(_columnIndexOfEspecialidad)
          }
          _item = UsuarioEntity(_tmpUsuarioId,_tmpRol,_tmpCorreo,_tmpContrasena,_tmpNombres,_tmpApellidoPaterno,_tmpApellidoMaterno,_tmpEspecialidad)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(usuarioId: String): UsuarioEntity? {
    val _sql: String = "SELECT * FROM usuario WHERE usuarioId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, usuarioId)
        val _columnIndexOfUsuarioId: Int = getColumnIndexOrThrow(_stmt, "usuarioId")
        val _columnIndexOfRol: Int = getColumnIndexOrThrow(_stmt, "rol")
        val _columnIndexOfCorreo: Int = getColumnIndexOrThrow(_stmt, "correo")
        val _columnIndexOfContrasena: Int = getColumnIndexOrThrow(_stmt, "contrasena")
        val _columnIndexOfNombres: Int = getColumnIndexOrThrow(_stmt, "nombres")
        val _columnIndexOfApellidoPaterno: Int = getColumnIndexOrThrow(_stmt, "apellidoPaterno")
        val _columnIndexOfApellidoMaterno: Int = getColumnIndexOrThrow(_stmt, "apellidoMaterno")
        val _columnIndexOfEspecialidad: Int = getColumnIndexOrThrow(_stmt, "especialidad")
        val _result: UsuarioEntity?
        if (_stmt.step()) {
          val _tmpUsuarioId: String
          _tmpUsuarioId = _stmt.getText(_columnIndexOfUsuarioId)
          val _tmpRol: String
          _tmpRol = _stmt.getText(_columnIndexOfRol)
          val _tmpCorreo: String
          _tmpCorreo = _stmt.getText(_columnIndexOfCorreo)
          val _tmpContrasena: String
          _tmpContrasena = _stmt.getText(_columnIndexOfContrasena)
          val _tmpNombres: String
          _tmpNombres = _stmt.getText(_columnIndexOfNombres)
          val _tmpApellidoPaterno: String
          _tmpApellidoPaterno = _stmt.getText(_columnIndexOfApellidoPaterno)
          val _tmpApellidoMaterno: String?
          if (_stmt.isNull(_columnIndexOfApellidoMaterno)) {
            _tmpApellidoMaterno = null
          } else {
            _tmpApellidoMaterno = _stmt.getText(_columnIndexOfApellidoMaterno)
          }
          val _tmpEspecialidad: String?
          if (_stmt.isNull(_columnIndexOfEspecialidad)) {
            _tmpEspecialidad = null
          } else {
            _tmpEspecialidad = _stmt.getText(_columnIndexOfEspecialidad)
          }
          _result = UsuarioEntity(_tmpUsuarioId,_tmpRol,_tmpCorreo,_tmpContrasena,_tmpNombres,_tmpApellidoPaterno,_tmpApellidoMaterno,_tmpEspecialidad)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun delete(usuarioId: String) {
    val _sql: String = "DELETE FROM usuario WHERE usuarioId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, usuarioId)
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
