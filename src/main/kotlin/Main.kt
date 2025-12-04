import java.util.Locale

// Equipe: Lucas Petenusso Viana, Gustavo Gimenez, Vinicius Bispo
// Análise de validade de argumento (questão 2) usando tabela verdade.

fun main() {

    // ============================================================
    // SEÇÃO 0 – CONFIGURAÇÃO INICIAL E ENTRADA DO USUÁRIO
    // ============================================================

    Locale.setDefault(Locale.US)

    println("Funcionários do metrô estão em greve? (S/N): ")
    val input = readln().trim().uppercase()
    val greveUsuario = input.startsWith("S")
    println(">> Entrada do usuário: greve = ${if (greveUsuario) "SIM" else "NÃO"}")
    println()

    // ============================================================
    // SEÇÃO 1 – DEFINIÇÃO DAS PROPOSIÇÕES, PREMISSAS E CONCLUSÃO
    // ============================================================
    //
    // Proposições:
    // p: metrô funciona dentro da normalidade
    // q: passageiros chegam no horário
    // r: há greve dos funcionários do metrô
    // m: metrô está em manutenção
    // t: transporte alternativo ativado
    // u: passageiros chegam com atraso
    // v: há excesso de passageiros nas estações
    // s: metrô fica lotado
    // w: satisfação dos passageiros é alta
    // x: há reclamações públicas
    // y: passageiros sentem desconforto
    //
    // Premissas:
    // 1) p -> q
    // 2) r -> (¬p ∧ s)
    // 3) (m ∨ r) -> t
    // 4) (r ∧ (t ∨ ¬t)) -> u
    // 5) (q ∧ ¬v) -> (w ∧ ¬x)
    // 6) u -> ¬w
    // 7) v -> s
    // 8) y ↔ s
    // 9) (¬w ∨ y) -> x
    // 10) r
    //
    // Conclusão:
    // C) x

    // Implementação da implicação lógica: (a -> b) ≡ (!a || b)
    fun implica(a: Boolean, b: Boolean): Boolean = !a || b

    // ============================================================
    // SEÇÃO 2 – CONFIGURAÇÃO DA TABELA VERDADE
    // ============================================================

    val numVars = 11                      // quantidade de proposições
    val totalLinhas = 1 shl numVars       // 2^11 = 2048 linhas

    // Flags de análise
    var existeContraExemplo = false       // premissas verdadeiras e conclusão falsa
    var formulaSempreVerdadeira = true    // (Premissas -> Conclusão) verdadeira em todas as linhas

    println("Tabela verdade do argumento (0 = Falso, 1 = Verdadeiro)")
    println("Lin | p q r m t u v s w x y | Premissas | Conclusao | (Premissas -> Conclusao)")
    println("----+-----------------------+----------+-----------+--------------------------")

    // ============================================================
    // SEÇÃO 3 – GERAÇÃO DAS COMBINAÇÕES E CÁLCULO DAS PREMISSAS
    // ============================================================

    for (linha in 0 until totalLinhas) {

        // 3.1 – Atribuição dos valores de verdade a partir dos bits de 'linha'
        val p = ((linha shr 0) and 1) == 1
        val q = ((linha shr 1) and 1) == 1
        val r = ((linha shr 2) and 1) == 1
        val m = ((linha shr 3) and 1) == 1
        val t = ((linha shr 4) and 1) == 1
        val u = ((linha shr 5) and 1) == 1
        val v = ((linha shr 6) and 1) == 1
        val s = ((linha shr 7) and 1) == 1
        val w = ((linha shr 8) and 1) == 1
        val x = ((linha shr 9) and 1) == 1
        val y = ((linha shr 10) and 1) == 1

        // 3.2 – Cálculo das premissas

        val prem1  = implica(p, q)              // 1) p -> q
        val prem2  = implica(r, (!p && s))      // 2) r -> (¬p ∧ s)
        val prem3  = implica(m || r, t)         // 3) (m ∨ r) -> t
        val prem4  = implica(r && (t || !t), u) // 4) (r ∧ (t ∨ ¬t)) -> u
        val prem5  = implica(q && !v, w && !x)  // 5) (q ∧ ¬v) -> (w ∧ ¬x)
        val prem6  = implica(u, !w)             // 6) u -> ¬w
        val prem7  = implica(v, s)              // 7) v -> s
        val prem8  = (y == s)                   // 8) y ↔ s
        val prem9  = implica(!w || y, x)        // 9) (¬w ∨ y) -> x
        val prem10 = r                          // 10) r

        val todasPremissas = prem1 && prem2 && prem3 && prem4 &&
                prem5 && prem6 && prem7 && prem8 && prem9 && prem10

        // Conclusão: x
        val conclusao = x

        // Implicação global: Premissas -> Conclusão
        val implicacaoGlobal = implica(todasPremissas, conclusao)

        // Atualização das flags globais
        if (!implicacaoGlobal) {
            formulaSempreVerdadeira = false
        }
        if (todasPremissas && !conclusao) {
            existeContraExemplo = true
        }

        // Impressão da linha da tabela (0/1 em vez de true/false)
        fun b(b: Boolean) = if (b) 1 else 0

        println(
            String.format(
                "%3d | %d %d %d %d %d %d %d %d %d %d %d |    %d     |     %d     |           %d",
                linha + 1,
                b(p), b(q), b(r), b(m), b(t), b(u), b(v), b(s), b(w), b(x), b(y),
                b(todasPremissas),
                b(conclusao),
                b(implicacaoGlobal)
            )
        )
    }

    // ============================================================
    // SEÇÃO 4 – RESULTADO FINAL (VALIDEZ E TAUTOLOGIA)
    // ============================================================

    println()

    val argumentoValido = !existeContraExemplo
    val eTautologia = formulaSempreVerdadeira

    println("RESULTADO FINAL:")
    println(
        "- Existe contraexemplo (premissas verdadeiras e conclusão falsa)? " +
                if (existeContraExemplo) "SIM" else "NÃO"
    )
    println("- O argumento é válido? " + if (argumentoValido) "SIM" else "NÃO")
    println("- A fórmula (Premissas -> Conclusão) é tautologia? " + if (eTautologia) "SIM" else "NÃO")

    println()
    println(
        "Observação: a entrada do usuário (greve = ${if (greveUsuario) "SIM" else "NÃO"}) " +
                "não interfere na análise de validade; a verificação é feita sobre todas as combinações possíveis."
    )
}
