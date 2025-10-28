/*
Projeto: Catfeina
Arquivo: widget_test.dart

Direitos autorais (c) 2025 Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:
Teste de widget para verificar a inicialização do aplicativo e a correta
injeção da configuração de flavor (AppConfig).
*/

import 'package:catfeina/app/core/di/app_config.dart';
import 'package:catfeina/app/core/di/app_config_catpersonal.dart';
import 'package:catfeina/app/core/di/app_providers.dart';
import 'package:catfeina/main.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('App inicializa e exibe o nome do flavor', (WidgetTester tester) async {
    // Cria uma instância da configuração para o teste.
    final AppConfig testConfig = CatpersonalAppConfig();

    // Constrói o app, sobrescrevendo o provider com a configuração de teste.
    await tester.pumpWidget(
      ProviderScope(
        overrides: [
          appConfigProvider.overrideWithValue(testConfig),
        ],
        child: const CatfeinaApp(),
      ),
    );

    // Verifica se o MaterialApp foi construído.
    final materialApp = find.byType(MaterialApp);
    expect(materialApp, findsOneWidget);

    // Verifica se o título do MaterialApp corresponde ao nome do flavor.
    final app = tester.widget<MaterialApp>(materialApp);
    expect(app.title, 'Catfeina');
  });
}
