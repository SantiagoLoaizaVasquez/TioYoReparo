package com.t2.appaws14753.`data`.local

import androidx.room3.InvalidationTracker
import androidx.room3.RoomOpenDelegate
import androidx.room3.migration.AutoMigrationSpec
import androidx.room3.migration.Migration
import androidx.room3.util.TableInfo
import androidx.room3.util.TableInfo.Companion.read
import androidx.room3.util.dropFtsSyncTriggers
import androidx.room3.util.performClear
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.t2.appaws14753.`data`.local.dao.ClienteDao
import com.t2.appaws14753.`data`.local.dao.ClienteDao_Impl
import com.t2.appaws14753.`data`.local.dao.DispositivoDao
import com.t2.appaws14753.`data`.local.dao.DispositivoDao_Impl
import com.t2.appaws14753.`data`.local.dao.OrdenDao
import com.t2.appaws14753.`data`.local.dao.OrdenDao_Impl
import com.t2.appaws14753.`data`.local.dao.ServicioDao
import com.t2.appaws14753.`data`.local.dao.ServicioDao_Impl
import com.t2.appaws14753.`data`.local.dao.UsuarioDao
import com.t2.appaws14753.`data`.local.dao.UsuarioDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room3.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL", "MemberExtensionConflict"])
internal class AppDatabase_Impl : AppDatabase() {
  private val _clienteDao: Lazy<ClienteDao> = lazy {
    ClienteDao_Impl(this)
  }

  private val _dispositivoDao: Lazy<DispositivoDao> = lazy {
    DispositivoDao_Impl(this)
  }

  private val _ordenDao: Lazy<OrdenDao> = lazy {
    OrdenDao_Impl(this)
  }

  private val _servicioDao: Lazy<ServicioDao> = lazy {
    ServicioDao_Impl(this)
  }

  private val _usuarioDao: Lazy<UsuarioDao> = lazy {
    UsuarioDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "c6448a9f4e3584e6f1ed851b7c98a149", "f5d99e4f7a413025b151a9660d6bf6c4") {
      public override suspend fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `clientes` (`id` TEXT NOT NULL, `nombre` TEXT NOT NULL, `email` TEXT NOT NULL, `telefono` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `dispositivo` (`dispositivoId` TEXT NOT NULL, `clienteId` TEXT NOT NULL, `marca` TEXT NOT NULL, `modelo` TEXT NOT NULL, `numeroSerie` TEXT NOT NULL, PRIMARY KEY(`dispositivoId`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `orden` (`ordenId` TEXT NOT NULL, `dispositivoId` TEXT NOT NULL, `clienteId` TEXT NOT NULL, `tecnicoId` TEXT NOT NULL, `tecnicoNombre` TEXT NOT NULL, `estado` TEXT NOT NULL, `prioridad` TEXT NOT NULL, `fechaIngreso` INTEGER NOT NULL, `fechaEntrega` INTEGER, `detalleDiagnostico` TEXT NOT NULL, `totalCobrado` REAL NOT NULL, `servicios` TEXT NOT NULL, PRIMARY KEY(`ordenId`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `servicio` (`servicioId` TEXT NOT NULL, `nombreServicio` TEXT NOT NULL, `precioServicio` REAL NOT NULL, PRIMARY KEY(`servicioId`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `usuario` (`usuarioId` TEXT NOT NULL, `rol` TEXT NOT NULL, `correo` TEXT NOT NULL, `contrasena` TEXT NOT NULL, `nombres` TEXT NOT NULL, `apellidoPaterno` TEXT NOT NULL, `apellidoMaterno` TEXT, `especialidad` TEXT, PRIMARY KEY(`usuarioId`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c6448a9f4e3584e6f1ed851b7c98a149')")
      }

      public override suspend fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `clientes`")
        connection.execSQL("DROP TABLE IF EXISTS `dispositivo`")
        connection.execSQL("DROP TABLE IF EXISTS `orden`")
        connection.execSQL("DROP TABLE IF EXISTS `servicio`")
        connection.execSQL("DROP TABLE IF EXISTS `usuario`")
      }

      public override suspend fun onCreate(connection: SQLiteConnection) {
      }

      public override suspend fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override suspend fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override suspend fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override suspend fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsClientes: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsClientes.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsClientes.put("nombre", TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsClientes.put("email", TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsClientes.put("telefono", TableInfo.Column("telefono", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysClientes: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesClientes: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoClientes: TableInfo = TableInfo("clientes", _columnsClientes, _foreignKeysClientes, _indicesClientes)
        val _existingClientes: TableInfo = read(connection, "clientes")
        if (!_infoClientes.equals(_existingClientes)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |clientes(com.t2.appaws14753.data.local.entity.ClienteEntity).
              | Expected:
              |""".trimMargin() + _infoClientes + """
              |
              | Found:
              |""".trimMargin() + _existingClientes)
        }
        val _columnsDispositivo: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsDispositivo.put("dispositivoId", TableInfo.Column("dispositivoId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDispositivo.put("clienteId", TableInfo.Column("clienteId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDispositivo.put("marca", TableInfo.Column("marca", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDispositivo.put("modelo", TableInfo.Column("modelo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDispositivo.put("numeroSerie", TableInfo.Column("numeroSerie", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysDispositivo: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesDispositivo: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoDispositivo: TableInfo = TableInfo("dispositivo", _columnsDispositivo, _foreignKeysDispositivo, _indicesDispositivo)
        val _existingDispositivo: TableInfo = read(connection, "dispositivo")
        if (!_infoDispositivo.equals(_existingDispositivo)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |dispositivo(com.t2.appaws14753.data.local.entity.DispositivoEntity).
              | Expected:
              |""".trimMargin() + _infoDispositivo + """
              |
              | Found:
              |""".trimMargin() + _existingDispositivo)
        }
        val _columnsOrden: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsOrden.put("ordenId", TableInfo.Column("ordenId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOrden.put("dispositivoId", TableInfo.Column("dispositivoId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOrden.put("clienteId", TableInfo.Column("clienteId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOrden.put("tecnicoId", TableInfo.Column("tecnicoId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOrden.put("tecnicoNombre", TableInfo.Column("tecnicoNombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOrden.put("estado", TableInfo.Column("estado", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOrden.put("prioridad", TableInfo.Column("prioridad", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOrden.put("fechaIngreso", TableInfo.Column("fechaIngreso", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOrden.put("fechaEntrega", TableInfo.Column("fechaEntrega", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOrden.put("detalleDiagnostico", TableInfo.Column("detalleDiagnostico", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOrden.put("totalCobrado", TableInfo.Column("totalCobrado", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsOrden.put("servicios", TableInfo.Column("servicios", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysOrden: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesOrden: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoOrden: TableInfo = TableInfo("orden", _columnsOrden, _foreignKeysOrden, _indicesOrden)
        val _existingOrden: TableInfo = read(connection, "orden")
        if (!_infoOrden.equals(_existingOrden)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |orden(com.t2.appaws14753.data.local.entity.OrdenEntity).
              | Expected:
              |""".trimMargin() + _infoOrden + """
              |
              | Found:
              |""".trimMargin() + _existingOrden)
        }
        val _columnsServicio: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsServicio.put("servicioId", TableInfo.Column("servicioId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsServicio.put("nombreServicio", TableInfo.Column("nombreServicio", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsServicio.put("precioServicio", TableInfo.Column("precioServicio", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysServicio: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesServicio: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoServicio: TableInfo = TableInfo("servicio", _columnsServicio, _foreignKeysServicio, _indicesServicio)
        val _existingServicio: TableInfo = read(connection, "servicio")
        if (!_infoServicio.equals(_existingServicio)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |servicio(com.t2.appaws14753.data.local.entity.ServicioEntity).
              | Expected:
              |""".trimMargin() + _infoServicio + """
              |
              | Found:
              |""".trimMargin() + _existingServicio)
        }
        val _columnsUsuario: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUsuario.put("usuarioId", TableInfo.Column("usuarioId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsuario.put("rol", TableInfo.Column("rol", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsuario.put("correo", TableInfo.Column("correo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsuario.put("contrasena", TableInfo.Column("contrasena", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsuario.put("nombres", TableInfo.Column("nombres", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsuario.put("apellidoPaterno", TableInfo.Column("apellidoPaterno", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsuario.put("apellidoMaterno", TableInfo.Column("apellidoMaterno", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsuario.put("especialidad", TableInfo.Column("especialidad", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUsuario: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUsuario: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUsuario: TableInfo = TableInfo("usuario", _columnsUsuario, _foreignKeysUsuario, _indicesUsuario)
        val _existingUsuario: TableInfo = read(connection, "usuario")
        if (!_infoUsuario.equals(_existingUsuario)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |usuario(com.t2.appaws14753.data.local.entity.UsuarioEntity).
              | Expected:
              |""".trimMargin() + _infoUsuario + """
              |
              | Found:
              |""".trimMargin() + _existingUsuario)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "clientes", "dispositivo", "orden", "servicio", "usuario")
  }

  public override suspend fun clearAllTables() {
    performClear(this, false, "clientes", "dispositivo", "orden", "servicio", "usuario")
  }

  protected override fun getRequiredColumnTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _columnTypeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _columnTypeConvertersMap.put(ClienteDao::class, ClienteDao_Impl.getRequiredColumnConverters())
    _columnTypeConvertersMap.put(DispositivoDao::class, DispositivoDao_Impl.getRequiredColumnConverters())
    _columnTypeConvertersMap.put(OrdenDao::class, OrdenDao_Impl.getRequiredColumnConverters())
    _columnTypeConvertersMap.put(ServicioDao::class, ServicioDao_Impl.getRequiredColumnConverters())
    _columnTypeConvertersMap.put(UsuarioDao::class, UsuarioDao_Impl.getRequiredColumnConverters())
    return _columnTypeConvertersMap
  }

  protected override fun getRequiredDaoReturnTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _daoReturnTypeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _daoReturnTypeConvertersMap.put(ClienteDao::class, ClienteDao_Impl.getRequiredDaoReturnTypeConverters())
    _daoReturnTypeConvertersMap.put(DispositivoDao::class, DispositivoDao_Impl.getRequiredDaoReturnTypeConverters())
    _daoReturnTypeConvertersMap.put(OrdenDao::class, OrdenDao_Impl.getRequiredDaoReturnTypeConverters())
    _daoReturnTypeConvertersMap.put(ServicioDao::class, ServicioDao_Impl.getRequiredDaoReturnTypeConverters())
    _daoReturnTypeConvertersMap.put(UsuarioDao::class, UsuarioDao_Impl.getRequiredDaoReturnTypeConverters())
    return _daoReturnTypeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun clienteDao(): ClienteDao = _clienteDao.value

  public override fun dispositivoDao(): DispositivoDao = _dispositivoDao.value

  public override fun ordenDao(): OrdenDao = _ordenDao.value

  public override fun servicioDao(): ServicioDao = _servicioDao.value

  public override fun usuarioDao(): UsuarioDao = _usuarioDao.value
}
