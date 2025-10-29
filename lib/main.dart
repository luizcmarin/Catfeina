/*
Projeto: Catfeina
Arquivo: main.dart

Direitos autorais (c) 2025 Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:
Ponto de entrada principal e widget raiz para o aplicativo Catfeina.
*/
import 'package:catfeina/app/core/di/app_config.dart';
import 'package:catfeina/app/core/di/app_providers.dart';
import 'package:catfeina/app/features/configuracoes/configuracoes_viewmodel.dart';
import 'package:catfeina/app/features/shell/shell_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

void run(AppConfig appConfig) {
  runApp(
    ProviderScope(
      overrides: [
        appConfigProvider.overrideWithValue(appConfig),
      ],
      child: const CatfeinaApp(),
    ),
  );
}

class CatfeinaApp extends ConsumerWidget {
  const CatfeinaApp({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final config = ref.watch(appConfigProvider);
    final configuracoesState = ref.watch(configuracoesViewModelProvider);

    return MaterialApp(
      title: config.appName,
      theme: config.theme,
      darkTheme: ThemeData.dark(), // Um tema escuro padrão, pode ser personalizado
      themeMode: configuracoesState.themeMode,
      textScaler: TextScaler.linear(configuracoesState.fatorEscalaTexto),
      home: const ShellScreen(),
    );
  }
}
