/*
Projeto: Catfeina
Arquivo: leitor_poesia_screen.dart

Direitos autorais (c) 2025 Marin. Todos os direitos reservados.

Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin

Este arquivo faz parte do projeto Catfeina.
A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
dele, é estritamente proibida.

Descrição:
Define a tela (View) que exibe o conteúdo detalhado de uma única poesia.
*/

import 'package:catfeina/app/features/leitor_poesia/leitor_poesia_state.dart';
import 'package:catfeina/app/features/leitor_poesia/leitor_poesia_viewmodel.dart';
import 'package:catfeina/app/features/som_ambiente/som_ambiente_viewmodel.dart';
import 'package:catfeina/app/features/tts/tts_player_widget.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class LeitorPoesiaScreen extends ConsumerWidget {
  final int poesiaId;

  const LeitorPoesiaScreen({super.key, required this.poesiaId});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(leitorPoesiaViewModelProvider(poesiaId));

    return Scaffold(
      appBar: AppBar(
        title: Text(state.status == LeitorPoesiaStatus.success
            ? state.poesia?.titulo ?? 'Carregando...'
            : 'Carregando...'),
        actions: [
          Consumer(
            builder: (context, ref, child) {
              final isPlaying = ref.watch(somAmbienteViewModelProvider);
              final viewModel = ref.read(somAmbienteViewModelProvider.notifier);
              return IconButton(
                icon: Icon(isPlaying ? Icons.volume_up : Icons.volume_off),
                onPressed: viewModel.toggle,
                tooltip: 'Som Ambiente',
              );
            },
          ),
          if (state.status == LeitorPoesiaStatus.success && state.poesia != null)
            TtsPlayerWidget(textoParaFalar: state.poesia!.texto),
        ],
      ),
      body: switch (state.status) {
        LeitorPoesiaStatus.loading ||
        LeitorPoesiaStatus.initial =>
          const Center(child: CircularProgressIndicator()),
        LeitorPoesiaStatus.failure =>
          Center(child: Text('Erro: ${state.errorMessage}')),
        LeitorPoesiaStatus.success => state.poesia == null
            ? const Center(child: Text('Poesia não encontrada.'))
            : SingleChildScrollView(
                padding: const EdgeInsets.all(16.0),
                child: Text(state.poesia!.texto),
              ),
      },
    );
  }
}
