/*
Projeto: Catfeina
Arquivo: main.dart

Direitos autorais (c) 2025 Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:
Ponto de entrada principal para o aplicativo Catfeina.
*/
import 'package:flutter/material.dart';

void main() {
  runApp(const CatfeinaApp());
}

class CatfeinaApp extends StatelessWidget {
  const CatfeinaApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Catfeina',
      home: Container(), // Placeholder for the home screen
    );
  }
}
