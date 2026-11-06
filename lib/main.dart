import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:pdf/pdf.dart';
import 'package:pdf/widgets.dart' as pw;
import 'package:printing/printing.dart';

import 'conexion.dart';
void main() {
  runApp(const AppTaller());
}

class AppTaller extends StatelessWidget {
  const AppTaller({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'GestorApp El Ultimo Arranque',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: const Color.fromARGB(255, 186, 25, 63)),
        useMaterial3: true,
      ),
      home: const PantallaMenuPrincipal(),
    );
  }
}

class PantallaMenuPrincipal extends StatelessWidget {
  const PantallaMenuPrincipal({super.key});

void _comprobarConexion(BuildContext context) async {
  showDialog(
    context: context,
    barrierDismissible: false,
    builder: (context) => const Center(child: CircularProgressIndicator()),
  );

  bool conectado = await ConexionBaseDatos.verificarConexion();

  if (!context.mounted) return;

  Navigator.pop(context);

  ScaffoldMessenger.of(context).showSnackBar(
    SnackBar(
      content: Text(
        conectado
            ? 'Conectado exitosamente a la base de datos taller.'
            : 'Error de conexión. Verifica la IP, XAMPP o el Wi-Fi.',
      ),
      backgroundColor: conectado ? Colors.green : Colors.red,
    ),
  );
}


  @override
  Widget build(BuildContext context) {
    final List<String> modulos = [
      'Órdenes de Repuestos',
      'Órdenes de Servicios',
      'Órdenes de Trabajo',
    ];

    final List<IconData> iconos = [
      Icons.build_circle_rounded,
      Icons.assignment_rounded,
      Icons.engineering_rounded,
    ];

final List<Widget> pantallas = [
  const PantallaOrdenesRepuestos(),
  const PantallaOrdenesServicios(),
  const PantallaOrdenesTrabajo(), 
];


    return Scaffold(
      appBar: AppBar(
        title: const Text('El Ultimo Arranque'),
        backgroundColor: Theme.of(context).colorScheme.primaryContainer,
        centerTitle: true,
        actions: [
          IconButton(
            icon: const Icon(Icons.cloud_sync_rounded),
            onPressed: () => _comprobarConexion(context),
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: GridView.builder(
          itemCount: modulos.length,
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 2,
            crossAxisSpacing: 16.0,
            mainAxisSpacing: 16.0,
            childAspectRatio: 1.2,
          ),
          itemBuilder: (context, index) {
            return Card(
              elevation: 4,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(16),
              ),
              child: InkWell(
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => pantallas[index]),
                  );
                },
                borderRadius: BorderRadius.circular(16),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(
                      iconos[index],
                      size: 44,
                      color: Theme.of(context).colorScheme.primary,
                    ),
                    const SizedBox(height: 12),
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 8.0),
                      child: Text(
                        modulos[index],
                        textAlign: TextAlign.center, 
                        style: const TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            );
          },
        ),
      ),
    );
  }
}


class PantallaOrdenesRepuestos extends StatefulWidget {
  const PantallaOrdenesRepuestos({super.key});

  @override
  State<PantallaOrdenesRepuestos> createState() =>
      _PantallaOrdenesRepuestosState();
}

class _PantallaOrdenesRepuestosState
    extends State<PantallaOrdenesRepuestos> {
  final String _urlApi =
      'http://192.168.0.17/taller_api/repuestos.php';

  List<dynamic> _registros = [];
  bool _cargando = true;

  @override
  void initState() {
    super.initState();
    _obtenerRegistros();
  }

  void _mensaje(String mensaje) {
    if (!mounted) return;
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(mensaje)),
    );
  }

  Future<void> _obtenerRegistros() async {
    if (!mounted) return;

    setState(() => _cargando = true);

    try {
      final respuesta = await http
          .get(Uri.parse(_urlApi))
          .timeout(const Duration(seconds: 15));

      debugPrint('ESTADO HTTP REPUESTOS: ${respuesta.statusCode}');
      debugPrint('RESPUESTA PHP: ${respuesta.body}');

      if (respuesta.statusCode == 200) {
        final datos = json.decode(respuesta.body);

        if (datos is List) {
          if (mounted) {
            setState(() {
              _registros = datos;
            });
          }
        } else {
          _mensaje('La respuesta de la API no es una lista JSON');
        }
      } else {
        _mensaje('Error HTTP: ${respuesta.statusCode}');
      }
    } catch (e) {
      debugPrint('ERROR AL CONSULTAR REPUESTOS: $e');
      _mensaje('No se pudieron consultar los repuestos');
    } finally {
      if (mounted) {
        setState(() {
          _cargando = false;
        });
      }
    }
  }

  Future<void> _guardarRegistro(
    Map<String, dynamic> datos,
    bool esEdicion,
  ) async {
    try {
      final headers = {
        'Content-Type': 'application/json',
      };

      final respuesta = esEdicion
          ? await http.put(
              Uri.parse(_urlApi),
              headers: headers,
              body: json.encode(datos),
            )
          : await http.post(
              Uri.parse(_urlApi),
              headers: headers,
              body: json.encode(datos),
            );

      if (respuesta.statusCode == 200) {
        _mensaje('Registro guardado correctamente');
        await _obtenerRegistros();
      } else {
        debugPrint('ERROR AL GUARDAR: ${respuesta.body}');
        _mensaje('No se pudo guardar el registro');
      }
    } catch (e) {
      debugPrint('ERROR AL GUARDAR REPUESTO: $e');
      _mensaje('Error de conexión al guardar');
    }
  }

  Future<void> _eliminarRegistro(int idDetalle) async {
    try {
      final respuesta = await http.delete(
        Uri.parse(
          '$_urlApi?id_detalle_repuesto=$idDetalle',
        ),
      );

      if (respuesta.statusCode == 200) {
        _mensaje('Registro eliminado correctamente');
        await _obtenerRegistros();
      } else {
        debugPrint('ERROR AL ELIMINAR: ${respuesta.body}');
        _mensaje('No se pudo eliminar el registro');
      }
    } catch (e) {
      debugPrint('ERROR AL ELIMINAR REPUESTO: $e');
      _mensaje('Error de conexión al eliminar');
    }
  }

  Future<void> _confirmarEliminar(int idDetalle) async {
    final confirmar = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Eliminar repuesto'),
        content: Text(
          '¿Deseas eliminar el detalle de repuesto #$idDetalle?',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context, false),
            child: const Text('Cancelar'),
          ),
          TextButton(
            onPressed: () => Navigator.pop(context, true),
            child: const Text(
              'Eliminar',
              style: TextStyle(color: Colors.red),
            ),
          ),
        ],
      ),
    );

    if (confirmar == true) {
      await _eliminarRegistro(idDetalle);
    }
  }

  Future<void> _generarPdfReporte(
    Map<String, dynamic> reg,
  ) async {
    final pdf = pw.Document();

    pdf.addPage(
      pw.Page(
        pageFormat: PdfPageFormat.roll80,
        margin: const pw.EdgeInsets.all(12),
        build: (pw.Context context) {
          return pw.Column(
            crossAxisAlignment: pw.CrossAxisAlignment.start,
            children: [
              pw.Center(
                child: pw.Text(
                  'EL ULTIMO ARRANQUE',
                  style: pw.TextStyle(
                    fontSize: 15,
                    fontWeight: pw.FontWeight.bold,
                  ),
                ),
              ),
              pw.SizedBox(height: 5),
              pw.Center(
                child: pw.Text(
                  'DETALLE DE REPUESTOS',
                  style: const pw.TextStyle(fontSize: 11),
                ),
              ),
              pw.Divider(),
              pw.Text(
                'Orden No. ${reg['id_orden']}',
                style: pw.TextStyle(
                  fontSize: 12,
                  fontWeight: pw.FontWeight.bold,
                ),
              ),
              pw.SizedBox(height: 8),
              pw.Text(
                'ID Detalle: ${reg['id_detalle_repuesto']}',
              ),
              pw.SizedBox(height: 5),
              pw.Text(
                'Repuesto ID: ${reg['id_repuesto']}',
              ),
              pw.SizedBox(height: 5),
              pw.Text(
                'Cantidad: ${reg['cantidad']}',
              ),
              pw.SizedBox(height: 5),
              pw.Text(
                'Precio Unitario: Q. ${reg['precio_unitario']}',
              ),
              pw.Divider(),
              pw.Text(
                'SUBTOTAL: Q. ${reg['subtotal']}',
                style: pw.TextStyle(
                  fontSize: 13,
                  fontWeight: pw.FontWeight.bold,
                ),
              ),
              pw.SizedBox(height: 15),
              pw.Center(
                child: pw.Text(
                  'Gracias por su preferencia',
                  style: const pw.TextStyle(fontSize: 9),
                ),
              ),
            ],
          );
        },
      ),
    );

    try {
      await Printing.layoutPdf(
        onLayout: (PdfPageFormat format) async {
          return pdf.save();
        },
      );
    } catch (e) {
      debugPrint('ERROR AL GENERAR PDF: $e');
      _mensaje('Error al generar el PDF');
    }
  }

  void _abrirFormulario({
    Map<String, dynamic>? item,
  }) {
    final bool esEdicion = item != null;

    final txtOrden = TextEditingController(
      text: esEdicion ? item['id_orden'].toString() : '',
    );

    final txtRepuesto = TextEditingController(
      text: esEdicion ? item['id_repuesto'].toString() : '',
    );

    final txtCantidad = TextEditingController(
      text: esEdicion ? item['cantidad'].toString() : '',
    );

    final txtPrecio = TextEditingController(
      text: esEdicion ? item['precio_unitario'].toString() : '',
    );

    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text(
          esEdicion ? 'Modificar Repuesto' : 'Nuevo Repuesto',
        ),
        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: txtOrden,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(
                  labelText: 'ID Orden',
                ),
              ),
              TextField(
                controller: txtRepuesto,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(
                  labelText: 'ID Repuesto',
                ),
              ),
              TextField(
                controller: txtCantidad,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(
                  labelText: 'Cantidad',
                ),
              ),
              TextField(
                controller: txtPrecio,
                keyboardType: const TextInputType.numberWithOptions(
                  decimal: true,
                ),
                decoration: const InputDecoration(
                  labelText: 'Precio Unitario',
                ),
              ),
            ],
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancelar'),
          ),
          TextButton(
            onPressed: () {
              final orden = int.tryParse(txtOrden.text);
              final repuesto = int.tryParse(txtRepuesto.text);
              final cantidad = int.tryParse(txtCantidad.text);
              final precio = double.tryParse(
                txtPrecio.text.replaceAll(',', '.'),
              );

              if (orden == null ||
                  repuesto == null ||
                  cantidad == null ||
                  precio == null ||
                  cantidad <= 0) {
                _mensaje('Verifica los datos ingresados');
                return;
              }

              final Map<String, dynamic> datos = {
                'id_orden': orden,
                'id_repuesto': repuesto,
                'cantidad': cantidad,
                'precio_unitario': precio,
              };

              if (esEdicion) {
                datos['id_detalle_repuesto'] =
                    item['id_detalle_repuesto'];
              }

              Navigator.pop(context);
              _guardarRegistro(datos, esEdicion);
            },
            child: const Text('Guardar'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Órdenes de Repuestos'),
        backgroundColor:
            Theme.of(context).colorScheme.primaryContainer,
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh_rounded),
            onPressed: _obtenerRegistros,
          ),
        ],
      ),
      body: _cargando
          ? const Center(
              child: CircularProgressIndicator(),
            )
          : _registros.isEmpty
              ? const Center(
                  child: Text('No hay registros en la base de datos.'),
                )
              : ListView.builder(
                  itemCount: _registros.length,
                  itemBuilder: (context, index) {
                    final item = _registros[index];

                    return Card(
                      margin: const EdgeInsets.symmetric(
                        horizontal: 12,
                        vertical: 6,
                      ),
                      child: ListTile(
                        title: Text(
                          'Orden #${item['id_orden']} - Repuesto ID: ${item['id_repuesto']}',
                        ),
                        subtitle: Text(
                          'Cantidad: ${item['cantidad']}'
                          ' | Precio: Q.${item['precio_unitario']}'
                          '\nSubtotal: Q.${item['subtotal']}',
                        ),
                        isThreeLine: true,
                        trailing: Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            IconButton(
                              icon: const Icon(
                                Icons.picture_as_pdf_rounded,
                                color: Colors.green,
                              ),
                              onPressed: () =>
                                  _generarPdfReporte(item),
                            ),
                            IconButton(
                              icon: const Icon(
                                Icons.edit,
                                color: Colors.blue,
                              ),
                              onPressed: () =>
                                  _abrirFormulario(item: item),
                            ),
                            IconButton(
                              icon: const Icon(
                                Icons.delete,
                                color: Colors.red,
                              ),
                              onPressed: () => _confirmarEliminar(
                                int.parse(
                                  item['id_detalle_repuesto'].toString(),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _abrirFormulario(),
        child: const Icon(Icons.add),
      ),
    );
  }
}



class PantallaOrdenesServicios extends StatefulWidget {
  const PantallaOrdenesServicios({super.key});

  @override
  State<PantallaOrdenesServicios> createState() =>
      _PantallaOrdenesServiciosState();
}

class _PantallaOrdenesServiciosState
    extends State<PantallaOrdenesServicios> {
  final String _urlApi =
      'http://192.168.0.17/taller_api/detalle_servicio.php';

  List<dynamic> _registros = [];
  bool _cargando = true;

  @override
  void initState() {
    super.initState();
    _obtenerRegistros();
  }

  void _mensaje(String mensaje) {
    if (!mounted) return;
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(mensaje)),
    );
  }

  Future<void> _obtenerRegistros() async {
    if (!mounted) return;

    setState(() => _cargando = true);

    try {
      final respuesta = await http
          .get(Uri.parse(_urlApi))
          .timeout(const Duration(seconds: 15));

      debugPrint('ESTADO HTTP SERVICIOS: ${respuesta.statusCode}');
      debugPrint('RESPUESTA PHP: ${respuesta.body}');

      if (respuesta.statusCode == 200) {
        final datos = json.decode(respuesta.body);

        if (datos is List) {
          if (mounted) {
            setState(() {
              _registros = datos;
            });
          }
        } else {
          _mensaje('La respuesta de la API no es una lista JSON');
        }
      } else {
        _mensaje('Error HTTP: ${respuesta.statusCode}');
      }
    } catch (e) {
      debugPrint('ERROR AL CONSULTAR SERVICIOS: $e');
      _mensaje('No se pudieron consultar los servicios');
    } finally {
      if (mounted) {
        setState(() {
          _cargando = false;
        });
      }
    }
  }

  Future<void> _guardarRegistro(
    Map<String, dynamic> datos,
    bool esEdicion,
  ) async {
    try {
      final headers = {
        'Content-Type': 'application/json',
      };

      final respuesta = esEdicion
          ? await http.put(
              Uri.parse(_urlApi),
              headers: headers,
              body: json.encode(datos),
            )
          : await http.post(
              Uri.parse(_urlApi),
              headers: headers,
              body: json.encode(datos),
            );

      if (respuesta.statusCode == 200) {
        _mensaje('Registro guardado correctamente');
        await _obtenerRegistros();
      } else {
        debugPrint('ERROR AL GUARDAR: ${respuesta.body}');
        _mensaje('No se pudo guardar el registro');
      }
    } catch (e) {
      debugPrint('ERROR AL GUARDAR SERVICIO: $e');
      _mensaje('Error de conexión al guardar');
    }
  }

  Future<void> _eliminarRegistro(int idDetalle) async {
    try {
      final respuesta = await http.delete(
        Uri.parse(
          '$_urlApi?id_detalle_servicio=$idDetalle',
        ),
      );

      if (respuesta.statusCode == 200) {
        _mensaje('Servicio eliminado correctamente');
        await _obtenerRegistros();
      } else {
        debugPrint('ERROR AL ELIMINAR: ${respuesta.body}');
        _mensaje('No se pudo eliminar el servicio');
      }
    } catch (e) {
      debugPrint('ERROR AL ELIMINAR SERVICIO: $e');
      _mensaje('Error de conexión al eliminar');
    }
  }

  Future<void> _confirmarEliminar(int idDetalle) async {
    final confirmar = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Eliminar servicio'),
        content: Text(
          '¿Deseas eliminar el detalle de servicio #$idDetalle?',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context, false),
            child: const Text('Cancelar'),
          ),
          TextButton(
            onPressed: () => Navigator.pop(context, true),
            child: const Text(
              'Eliminar',
              style: TextStyle(color: Colors.red),
            ),
          ),
        ],
      ),
    );

    if (confirmar == true) {
      await _eliminarRegistro(idDetalle);
    }
  }

  Future<void> _generarPdfReporte(
    Map<String, dynamic> reg,
  ) async {
    final pdf = pw.Document();

    pdf.addPage(
      pw.Page(
        pageFormat: PdfPageFormat.roll80,
        margin: const pw.EdgeInsets.all(12),
        build: (pw.Context context) {
          return pw.Column(
            crossAxisAlignment: pw.CrossAxisAlignment.start,
            children: [
              pw.Center(
                child: pw.Text(
                  'EL ULTIMO ARRANQUE',
                  style: pw.TextStyle(
                    fontSize: 15,
                    fontWeight: pw.FontWeight.bold,
                  ),
                ),
              ),
              pw.SizedBox(height: 5),
              pw.Center(
                child: pw.Text(
                  'DETALLE DE SERVICIO',
                  style: const pw.TextStyle(fontSize: 11),
                ),
              ),
              pw.Divider(),
              pw.Text(
                'Orden No. ${reg['id_orden']}',
                style: pw.TextStyle(
                  fontSize: 12,
                  fontWeight: pw.FontWeight.bold,
                ),
              ),
              pw.SizedBox(height: 8),
              pw.Text(
                'ID Detalle: ${reg['id_detalle_servicio']}',
              ),
              pw.SizedBox(height: 5),
              pw.Text(
                'Servicio ID: ${reg['id_servicio']}',
              ),
              pw.SizedBox(height: 5),
              pw.Text(
                'Costo Aplicado: Q. ${reg['costo_aplicado']}',
              ),
              pw.Divider(),
              pw.Text(
                'TOTAL: Q. ${reg['costo_aplicado']}',
                style: pw.TextStyle(
                  fontSize: 13,
                  fontWeight: pw.FontWeight.bold,
                ),
              ),
              pw.SizedBox(height: 15),
              pw.Center(
                child: pw.Text(
                  'Gracias por su preferencia',
                  style: const pw.TextStyle(fontSize: 9),
                ),
              ),
            ],
          );
        },
      ),
    );

    try {
      await Printing.layoutPdf(
        onLayout: (PdfPageFormat format) async {
          return pdf.save();
        },
      );
    } catch (e) {
      debugPrint('ERROR AL GENERAR PDF: $e');
      _mensaje('Error al generar el PDF');
    }
  }

  void _abrirFormulario({
    Map<String, dynamic>? item,
  }) {
    final bool esEdicion = item != null;

    final txtOrden = TextEditingController(
      text: esEdicion ? item['id_orden'].toString() : '',
    );

    final txtServicio = TextEditingController(
      text: esEdicion ? item['id_servicio'].toString() : '',
    );

    final txtCosto = TextEditingController(
      text: esEdicion ? item['costo_aplicado'].toString() : '',
    );

    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text(
          esEdicion ? 'Modificar Servicio' : 'Nuevo Servicio',
        ),
        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: txtOrden,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(
                  labelText: 'ID Orden',
                ),
              ),
              TextField(
                controller: txtServicio,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(
                  labelText: 'ID Servicio',
                ),
              ),
              TextField(
                controller: txtCosto,
                keyboardType: const TextInputType.numberWithOptions(
                  decimal: true,
                ),
                decoration: const InputDecoration(
                  labelText: 'Costo Aplicado',
                ),
              ),
            ],
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancelar'),
          ),
          TextButton(
            onPressed: () {
              final orden = int.tryParse(txtOrden.text);
              final servicio = int.tryParse(txtServicio.text);
              final costo = double.tryParse(
                txtCosto.text.replaceAll(',', '.'),
              );

              if (orden == null ||
                  servicio == null ||
                  costo == null) {
                _mensaje('Verifica los datos ingresados');
                return;
              }

              final Map<String, dynamic> datos = {
                'id_orden': orden,
                'id_servicio': servicio,
                'costo_aplicado': costo,
              };

              if (esEdicion) {
                datos['id_detalle_servicio'] =
                    item['id_detalle_servicio'];
              }

              Navigator.pop(context);
              _guardarRegistro(datos, esEdicion);
            },
            child: const Text('Guardar'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Órdenes de Servicios'),
        backgroundColor:
            Theme.of(context).colorScheme.primaryContainer,
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh_rounded),
            onPressed: _obtenerRegistros,
          ),
        ],
      ),
      body: _cargando
          ? const Center(
              child: CircularProgressIndicator(),
            )
          : _registros.isEmpty
              ? const Center(
                  child: Text('No hay registros en la base de datos.'),
                )
              : ListView.builder(
                  itemCount: _registros.length,
                  itemBuilder: (context, index) {
                    final item = _registros[index];

                    return Card(
                      margin: const EdgeInsets.symmetric(
                        horizontal: 12,
                        vertical: 6,
                      ),
                      child: ListTile(
                        title: Text(
                          'Orden #${item['id_orden']} - Servicio ID: ${item['id_servicio']}',
                        ),
                        subtitle: Text(
                          'Costo Aplicado: Q.${item['costo_aplicado']}',
                        ),
                        trailing: Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            IconButton(
                              icon: const Icon(
                                Icons.picture_as_pdf_rounded,
                                color: Colors.green,
                              ),
                              onPressed: () =>
                                  _generarPdfReporte(item),
                            ),
                            IconButton(
                              icon: const Icon(
                                Icons.edit,
                                color: Colors.blue,
                              ),
                              onPressed: () =>
                                  _abrirFormulario(item: item),
                            ),
                            IconButton(
                              icon: const Icon(
                                Icons.delete,
                                color: Colors.red,
                              ),
                              onPressed: () => _confirmarEliminar(
                                int.parse(
                                  item['id_detalle_servicio'].toString(),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _abrirFormulario(),
        child: const Icon(Icons.add),
      ),
    );
  }
}


class PantallaOrdenesTrabajo extends StatefulWidget {
  const PantallaOrdenesTrabajo({super.key});

  @override
  State<PantallaOrdenesTrabajo> createState() =>
      _PantallaOrdenesTrabajoState();
}

class _PantallaOrdenesTrabajoState
    extends State<PantallaOrdenesTrabajo> {

  final String _urlApi =
      'http://192.168.0.17/taller_api/ordenes_trabajo.php';

  List<dynamic> _registros = [];
  bool _cargando = true;

  @override
  void initState() {
    super.initState();
    _obtenerRegistros();
  }

  Future<void> _obtenerRegistros() async {
    if (!mounted) return;

    setState(() => _cargando = true);

    try {
      final respuesta = await http
          .get(Uri.parse(_urlApi))
          .timeout(const Duration(seconds: 15));

      debugPrint('ESTADO HTTP: ${respuesta.statusCode}');
      debugPrint('RESPUESTA PHP: ${respuesta.body}');

      if (respuesta.statusCode == 200) {
        final datos = json.decode(respuesta.body);

        if (datos is List) {
          if (mounted) {
            setState(() {
              _registros = datos;
            });
          }
        } else {
          debugPrint('ERROR: La respuesta no es una lista JSON');
        }
      } else {
        debugPrint('ERROR HTTP: ${respuesta.statusCode}');
      }

    } catch (e) {
      debugPrint('ERROR AL CONSULTAR API: $e');

    } finally {
      if (mounted) {
        setState(() {
          _cargando = false;
        });
      }
    }
  }


  Future<void> _guardarRegistro(
      Map<String, dynamic> datos,
      bool esEdicion) async {

    try {
      final headers = {
        'Content-Type': 'application/json',
      };

      final body = json.encode(datos);

      final respuesta = esEdicion
          ? await http.put(
              Uri.parse(_urlApi),
              headers: headers,
              body: body,
            )
          : await http.post(
              Uri.parse(_urlApi),
              headers: headers,
              body: body,
            );

      if (respuesta.statusCode == 200) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Registro guardado correctamente'),
            ),
          );
        }

        await _obtenerRegistros();
      } else {
        debugPrint('ERROR AL GUARDAR: ${respuesta.body}');
      }

    } catch (e) {
      debugPrint('ERROR AL GUARDAR REGISTRO: $e');
    }
  }


  Future<void> _eliminarRegistro(int idOrden) async {
    try {
      final respuesta = await http.delete(
        Uri.parse('$_urlApi?id_orden=$idOrden'),
      );

      if (respuesta.statusCode == 200) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Orden eliminada correctamente'),
            ),
          );
        }

        await _obtenerRegistros();
      } else {
        debugPrint('ERROR AL ELIMINAR: ${respuesta.body}');
      }

    } catch (e) {
      debugPrint('ERROR AL ELIMINAR: $e');
    }
  }


  Future<void> _confirmarEliminar(int idOrden) async {
    final confirmar = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Eliminar orden'),
        content: Text(
          '¿Deseas eliminar la orden #$idOrden?',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context, false),
            child: const Text('Cancelar'),
          ),
          TextButton(
            onPressed: () => Navigator.pop(context, true),
            child: const Text(
              'Eliminar',
              style: TextStyle(color: Colors.red),
            ),
          ),
        ],
      ),
    );

    if (confirmar == true) {
      await _eliminarRegistro(idOrden);
    }
  }



  Future<void> _generarPdfReporte(
      Map<String, dynamic> reg) async {

    final pdf = pw.Document();

    pdf.addPage(
      pw.Page(
        pageFormat: PdfPageFormat.roll80,
        margin: const pw.EdgeInsets.all(12),

        build: (pw.Context context) {
          return pw.Column(
            crossAxisAlignment: pw.CrossAxisAlignment.start,
            children: [

              pw.Center(
                child: pw.Text(
                  'EL ULTIMO ARRANQUE',
                  style: pw.TextStyle(
                    fontSize: 15,
                    fontWeight: pw.FontWeight.bold,
                  ),
                ),
              ),

              pw.SizedBox(height: 5),

              pw.Center(
                child: pw.Text(
                  'ORDEN DE TRABAJO',
                  style: const pw.TextStyle(
                    fontSize: 11,
                  ),
                ),
              ),

              pw.Divider(),

              pw.Text(
                'Orden No. ${reg['id_orden']}',
                style: pw.TextStyle(
                  fontSize: 12,
                  fontWeight: pw.FontWeight.bold,
                ),
              ),

              pw.SizedBox(height: 8),

              pw.Text(
                'Vehiculo: ${reg['id_vehiculo']}',
              ),

              pw.Text(
                'Mecanico: ${reg['id_mecanico']}',
              ),

              pw.SizedBox(height: 5),

              pw.Text(
                'Fecha ingreso: ${reg['fecha_ingreso']}',
              ),

              pw.Text(
                'Fecha entrega: ${reg['fecha_entrega']}',
              ),

              pw.Divider(),

              pw.Text(
                'DESCRIPCION DEL PROBLEMA',
                style: pw.TextStyle(
                  fontWeight: pw.FontWeight.bold,
                ),
              ),

              pw.SizedBox(height: 4),

              pw.Text(
                '${reg['descripcion_problema']}',
              ),

              pw.SizedBox(height: 10),

              pw.Text(
                'Estado: ${reg['estado']}',
              ),

              pw.SizedBox(height: 8),

              pw.Divider(),

              pw.Text(
                'TOTAL: Q. ${reg['costo_total']}',
                style: pw.TextStyle(
                  fontSize: 13,
                  fontWeight: pw.FontWeight.bold,
                ),
              ),

              pw.SizedBox(height: 15),

              pw.Center(
                child: pw.Text(
                  'Gracias por su preferencia',
                  style: const pw.TextStyle(
                    fontSize: 9,
                  ),
                ),
              ),
            ],
          );
        },
      ),
    );

    try {
      await Printing.layoutPdf(
        onLayout: (PdfPageFormat format) async {
          return pdf.save();
        },
      );
    } catch (e) {
      debugPrint('ERROR AL GENERAR PDF: $e');
    }
  }

  void _abrirFormulario({
    Map<String, dynamic>? item,
  }) {

    final bool esEdicion = item != null;

    final txtVehiculo = TextEditingController(
      text: esEdicion
          ? item['id_vehiculo'].toString()
          : '',
    );

    final txtMecanico = TextEditingController(
      text: esEdicion
          ? item['id_mecanico'].toString()
          : '',
    );

    final txtIngreso = TextEditingController(
      text: esEdicion
          ? item['fecha_ingreso'].toString()
          : '',
    );

    final txtEntrega = TextEditingController(
      text: esEdicion
          ? item['fecha_entrega'].toString()
          : '',
    );

    final txtDescripcion = TextEditingController(
      text: esEdicion
          ? item['descripcion_problema'].toString()
          : '',
    );

    final txtEstado = TextEditingController(
      text: esEdicion
          ? item['estado'].toString()
          : '',
    );

    final txtTotal = TextEditingController(
      text: esEdicion
          ? item['costo_total'].toString()
          : '',
    );

    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text(
          esEdicion
              ? 'Modificar Orden'
              : 'Nueva Orden',
        ),

        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [

              TextField(
                controller: txtVehiculo,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(
                  labelText: 'ID Vehículo',
                ),
              ),

              TextField(
                controller: txtMecanico,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(
                  labelText: 'ID Mecánico',
                ),
              ),

              TextField(
                controller: txtIngreso,
                decoration: const InputDecoration(
                  labelText: 'Fecha Ingreso',
                ),
              ),

              TextField(
                controller: txtEntrega,
                decoration: const InputDecoration(
                  labelText: 'Fecha Entrega',
                ),
              ),

              TextField(
                controller: txtDescripcion,
                decoration: const InputDecoration(
                  labelText: 'Descripción del Problema',
                ),
              ),

              TextField(
                controller: txtEstado,
                decoration: const InputDecoration(
                  labelText: 'Estado',
                ),
              ),

              TextField(
                controller: txtTotal,
                keyboardType: const TextInputType.numberWithOptions(
                  decimal: true,
                ),
                decoration: const InputDecoration(
                  labelText: 'Costo Total',
                ),
              ),
            ],
          ),
        ),

        actions: [

          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancelar'),
          ),

          TextButton(
            onPressed: () {

              try {
                final datos = <String, dynamic>{
                  'id_vehiculo': int.parse(txtVehiculo.text),
                  'id_mecanico': int.parse(txtMecanico.text),
                  'fecha_ingreso': txtIngreso.text,
                  'fecha_entrega': txtEntrega.text,
                  'descripcion_problema': txtDescripcion.text,
                  'estado': txtEstado.text,
                  'costo_total': double.parse(
                    txtTotal.text.replaceAll(',', '.'),
                  ),
                };

                if (esEdicion) {
                  datos['id_orden'] = item['id_orden'];
                }

                Navigator.pop(context);

                _guardarRegistro(datos, esEdicion);

              } catch (e) {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(
                    content: Text(
                      'Verifica los campos numéricos',
                    ),
                  ),
                );
              }
            },

            child: const Text('Guardar'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(

      appBar: AppBar(
        title: const Text('Órdenes de Trabajo'),

        backgroundColor:
            Theme.of(context).colorScheme.primaryContainer,

        actions: [

          IconButton(
            icon: const Icon(Icons.refresh_rounded),
            tooltip: 'Actualizar órdenes',
            onPressed: _obtenerRegistros,
          ),

        ],
      ),

      body: _cargando

          ? const Center(
              child: CircularProgressIndicator(),
            )

          : _registros.isEmpty

              ? const Center(
                  child: Text(
                    'No hay registros en la base de datos.',
                  ),
                )

              : ListView.builder(

                  itemCount: _registros.length,

                  itemBuilder: (context, index) {

                    final item = _registros[index];

                    return Card(

                      margin: const EdgeInsets.symmetric(
                        horizontal: 12,
                        vertical: 6,
                      ),

                      child: ListTile(

                        title: Text(
                          'Orden #${item['id_orden']} - Estado: ${item['estado']}',
                        ),

                        subtitle: Text(
                          'Vehículo: ${item['id_vehiculo']} | '
                          'Mecánico: ${item['id_mecanico']}\n'
                          'Problema: ${item['descripcion_problema']}\n'
                          'Total: Q.${item['costo_total']}',
                        ),

                        isThreeLine: true,

                        trailing: Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [

                            // IMPRIMIR ORDEN INDIVIDUAL

                            IconButton(
                              icon: const Icon(
                                Icons.print,
                                color: Colors.green,
                              ),

                              tooltip: 'Imprimir orden',

                              onPressed: () {
                                _generarPdfReporte(item);
                              },
                            ),

                            // EDITAR ORDEN

                            IconButton(
                              icon: const Icon(
                                Icons.edit,
                                color: Colors.blue,
                              ),

                              tooltip: 'Editar orden',

                              onPressed: () {
                                _abrirFormulario(item: item);
                              },
                            ),

                            // ELIMINAR ORDEN

                            IconButton(
                              icon: const Icon(
                                Icons.delete,
                                color: Colors.red,
                              ),

                              tooltip: 'Eliminar orden',

                              onPressed: () {
                                _confirmarEliminar(
                                  int.parse(
                                    item['id_orden'].toString(),
                                  ),
                                );
                              },
                            ),

                          ],
                        ),
                      ),
                    );
                  },
                ),

      floatingActionButton: FloatingActionButton(
        onPressed: () => _abrirFormulario(),
        child: const Icon(Icons.add),
      ),
    );
  }
}



