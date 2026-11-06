import 'package:http/http.dart' as http;
import 'dart:convert';

class ConexionBaseDatos {

  static const String _baseUrl =
      'http://192.168.0.17/taller_api';

  static Future<bool> verificarConexion() async {
    try {

      final url = Uri.parse(
        '$_baseUrl/probar_conexion.php'
      );

      final respuesta = await http
          .get(url)
          .timeout(const Duration(seconds: 15));

      print('Código HTTP: ${respuesta.statusCode}');
      print('Respuesta PHP: ${respuesta.body}');

      if (respuesta.statusCode == 200) {

        final datos = jsonDecode(respuesta.body);

        return datos['estado'] == 'conectado';
      }

      return false;

    } catch (e) {

      print('Error al intentar conectar: $e');

      return false;
    }
  }
}

