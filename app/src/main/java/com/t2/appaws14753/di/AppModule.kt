package com.t2.appaws14753.di

import android.content.Context
import com.t2.appaws14753.data.local.AppDatabase
import com.t2.appaws14753.domain.model.Roles
import com.t2.appaws14753.domain.model.Usuario
import com.t2.appaws14753.data.repository.ClienteRepositoryImpl
import com.t2.appaws14753.data.repository.DispositivoRepositoryImpl
import com.t2.appaws14753.data.repository.OrdenRepositoryImpl
import com.t2.appaws14753.data.repository.ServicioRepositoryImpl
import com.t2.appaws14753.data.repository.UsuarioRepositoryImpl
import com.t2.appaws14753.domain.repository.ClienteRepository
import com.t2.appaws14753.domain.repository.DispositivoRepository
import com.t2.appaws14753.domain.repository.OrdenRepository
import com.t2.appaws14753.domain.repository.ServicioRepository
import com.t2.appaws14753.domain.repository.UsuarioRepository
import com.t2.appaws14753.domain.usecase.dispositivo.ActualizarDispositivoUseCase
import com.t2.appaws14753.domain.usecase.dispositivo.DispositivoUseCases
import com.t2.appaws14753.domain.usecase.dispositivo.EliminarDispositivoUseCase
import com.t2.appaws14753.domain.usecase.dispositivo.GetDispositivoByIdUseCase
import com.t2.appaws14753.domain.usecase.dispositivo.GetDispositivoUseCase
import com.t2.appaws14753.domain.usecase.dispositivo.InsertarDispositivoUseCase
import com.t2.appaws14753.domain.usecase.cliente.ActualizarClienteUseCase
import com.t2.appaws14753.domain.usecase.cliente.ClienteUseCases
import com.t2.appaws14753.domain.usecase.cliente.EliminarClienteUseCase
import com.t2.appaws14753.domain.usecase.cliente.GetClienteByIdUseCase
import com.t2.appaws14753.domain.usecase.cliente.GetClienteUseCase
import com.t2.appaws14753.domain.usecase.cliente.InsertarClienteUseCase
import com.t2.appaws14753.domain.usecase.orden.ActualizarOrdenUseCase
import com.t2.appaws14753.domain.usecase.orden.EliminarOrdenUseCase
import com.t2.appaws14753.domain.usecase.orden.GetOrdenByIdUseCase
import com.t2.appaws14753.domain.usecase.orden.GetOrdenUseCase
import com.t2.appaws14753.domain.usecase.orden.InsertarOrdenUseCase
import com.t2.appaws14753.domain.usecase.orden.OrdenUseCases
import com.t2.appaws14753.domain.usecase.servicio.EliminarServicioUseCase
import com.t2.appaws14753.domain.usecase.servicio.GetServicioByIdUseCase
import com.t2.appaws14753.domain.usecase.servicio.GetServicioUseCase
import com.t2.appaws14753.domain.usecase.servicio.InsertarServicioUseCase
import com.t2.appaws14753.domain.usecase.servicio.ServicioUseCases
import com.t2.appaws14753.domain.usecase.servicio.UpdateServicioUseCase
import com.t2.appaws14753.domain.usecase.usuario.ActualizarUsuarioUseCase
import com.t2.appaws14753.domain.usecase.usuario.EliminarUsuarioUseCase
import com.t2.appaws14753.domain.usecase.usuario.GetUsuarioByIdUseCase
import com.t2.appaws14753.domain.usecase.usuario.GetUsuarioUseCase
import com.t2.appaws14753.domain.usecase.usuario.InsertarUsuarioUseCase
import com.t2.appaws14753.domain.usecase.usuario.UsuarioUseCases

object AppModule {

    // Database

    private fun provideDatabase(context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    // DAOs

    private fun provideClienteDao(context: Context) = provideDatabase(context).clienteDao()
    private fun provideDispositivoDao(context: Context) = provideDatabase(context).dispositivoDao()
    private fun provideOrdenDao(context: Context) = provideDatabase(context).ordenDao()
    private fun provideServicioDao(context: Context) = provideDatabase(context).servicioDao()
    private fun provideUsuarioDao(context: Context) = provideDatabase(context).usuarioDao()


    private fun provideClienteRepository(context: Context): ClienteRepository =
        ClienteRepositoryImpl(provideClienteDao(context))

    private fun provideDispositivoRepository(context: Context): DispositivoRepository =
        DispositivoRepositoryImpl(provideDispositivoDao(context))

    private fun provideOrdenRepository(context: Context): OrdenRepository =
        OrdenRepositoryImpl(provideOrdenDao(context))

    private fun provideServicioRepository(context: Context): ServicioRepository =
        ServicioRepositoryImpl(provideServicioDao(context))

    private fun provideUsuarioRepository(context: Context): UsuarioRepository =
        UsuarioRepositoryImpl(provideUsuarioDao(context))


    fun provideClienteUseCases(context: Context): ClienteUseCases {
        val repository = provideClienteRepository(context)
        return ClienteUseCases(
            getClientes = GetClienteUseCase(repository),
            getClienteById = GetClienteByIdUseCase(repository),
            insertarCliente = InsertarClienteUseCase(repository),
            actualizarCliente = ActualizarClienteUseCase(repository),
            eliminarCliente = EliminarClienteUseCase(repository)
        )
    }

    fun provideOrdenUseCases(context: Context): OrdenUseCases {
        val repository = provideOrdenRepository(context)
        return OrdenUseCases(
            getOrdenes = GetOrdenUseCase(repository),
            getOrdenById = GetOrdenByIdUseCase(repository),
            insertarOrden = InsertarOrdenUseCase(repository),
            actualizarOrden = ActualizarOrdenUseCase(repository),
            eliminarOrden = EliminarOrdenUseCase(repository)
        )
    }

    fun provideServicioUseCases(context: Context): ServicioUseCases {
        val repository = provideServicioRepository(context)
        return ServicioUseCases(
            getServicios = GetServicioUseCase(repository),
            getServicioById = GetServicioByIdUseCase(repository),
            insertarServicio = InsertarServicioUseCase(repository),
            actualizarServicio = UpdateServicioUseCase(repository),
            eliminarServicio = EliminarServicioUseCase(repository)
        )
    }

    fun provideUsuarioUseCases(context: Context): UsuarioUseCases {
        val repository = provideUsuarioRepository(context)
        return UsuarioUseCases(
            getUsuarios = GetUsuarioUseCase(repository),
            getUsuarioById = GetUsuarioByIdUseCase(repository),
            insertarUsuario = InsertarUsuarioUseCase(repository),
            actualizarUsuario = ActualizarUsuarioUseCase(repository),
            eliminarUsuario = EliminarUsuarioUseCase(repository)
        )
    }

    fun provideDispositivoRepositoryPublic(context: Context): DispositivoRepository =
        provideDispositivoRepository(context)

    fun provideDispositivoUseCases(context: Context): DispositivoUseCases {
        val repository = provideDispositivoRepository(context)
        return DispositivoUseCases(
            getDispositivos = GetDispositivoUseCase(repository),
            getDispositivoById = GetDispositivoByIdUseCase(repository),
            insertarDispositivo = InsertarDispositivoUseCase(repository),
            actualizarDispositivo = ActualizarDispositivoUseCase(repository),
            eliminarDispositivo = EliminarDispositivoUseCase(repository)
        )
    }

    // Semilla inicial: si todavía no hay usuarios en la base de datos real,
    // crea un admin por defecto para poder entrar y usar UsuariosScreen.
    suspend fun seedAdminInicial(context: Context) {
        val usuarioUseCases = provideUsuarioUseCases(context)
        val usuarios = usuarioUseCases.getUsuarios()
        if (usuarios.isEmpty()) {
            usuarioUseCases.insertarUsuario(
                Usuario(
                    rol = Roles.ADMIN,
                    correo = "admin@tioyoreparo.com",
                    contrasena = "admin123",
                    nombres = "Administrador",
                    apellidoPaterno = "Principal"
                )
            )
        }
    }
}
