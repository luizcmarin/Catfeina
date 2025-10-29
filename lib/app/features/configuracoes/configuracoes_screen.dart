/*
Projeto: Catfeina
Arquivo: configuracoes_screen.dart

Direitos autorais (c) 2025 Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:
Define a tela (View) de configurações, permitindo ao usuário personalizar
aspectos do aplicativo, como o modo de tema e o tamanho da fonte.
*/

import 'package:catfeina/app/features/configuracoes/configuracoes_viewmodel.dart';
import 'package:catfeina/app/features/depuracao/depuracao_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class ConfiguracoesScreen extends ConsumerWidget {
  const ConfiguracoesScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(configuracoesViewModelProvider);
    final viewModel = ref.read(configuracoesViewModelProvider.notifier);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Configurações'),
      ),
      body: ListView(
        padding: const EdgeInsets.all(16.0),
        children: [
          Text(
            'Tema do Aplicativo',
            style: Theme.of(context).textTheme.titleLarge,
          ),
          const SizedBox(height: 8),
          SegmentedButton<ThemeMode>(
            segments: const <ButtonSegment<ThemeMode>>[
              ButtonSegment(
                value: ThemeMode.light,
                label: Text('Claro'),
                icon: Icon(Icons.wb_sunny),
              ),
              ButtonSegment(
                value: ThemeMode.dark,
                label: Text('Escuro'),
                icon: Icon(Icons.nightlight_round),
              ),
              ButtonSegment(
                value: ThemeMode.system,
                label: Text('Sistema'),
                icon: Icon(Icons.settings_suggest),
              ),
            ],
            selected: {state.themeMode},
            onSelectionChanged: (Set<ThemeMode> newSelection) {
              viewModel.mudarTema(newSelection.first);
            },
          ),
          const Divider(height: 40),
          Text(
            'Tamanho do Texto',
            style: Theme.of(context).textTheme.titleLarge,
          ),
          const SizedBox(height: 8),
          Slider(
            value: state.fatorEscalaTexto,
            min: 0.8,
            max: 1.5,
            divisions: 7,
            label: state.fatorEscalaTexto.toStringAsFixed(1),
            onChanged: (value) {
              viewModel.mudarEscalaTexto(value);
            },
          ),
          const Divider(height: 40),
          ListTile(
            leading: const Icon(Icons.bug_report),
            title: const Text('Opções de Depuração'),
            subtitle: const Text('Informações internas do app'),
            onTap: () {
              Navigator.of(context).push(
                MaterialPageRoute(
                  builder: (context) => const DepuracaoScreen(),
                ),
              );
            },
          ),
        ],
      ),
    );
  }
}
