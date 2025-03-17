import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.relogio.R

// Classe que gerencia os sons no jogo
class SoundPlayer(private val context: Context) {

    // Inicializa o SoundPool e um mapa para armazenar os sons
    private val soundPool: SoundPool
    private val soundMap = HashMap<String, Int>()

    // Bloco init é executado quando a classe é instanciada
    init {
        // Define os atributos de áudio para o SoundPool (uso em jogos e sons de interação)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)  // Uso para sons de jogos
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)  // Sons para sonificação (efeitos sonoros)
            .build()

        // Cria o SoundPool com as configurações desejadas
        soundPool = SoundPool.Builder()
            .setMaxStreams(3) // Define o número máximo de sons que podem ser tocados simultaneamente
            .setAudioAttributes(audioAttributes)  // Define os atributos de áudio configurados anteriormente
            .build()

        // Carrega os sons no SoundPool
        // A função `load` carrega os arquivos de som para o SoundPool.
        // Aqui, os sons "jump", "death" e "score" são carregados a partir de arquivos de recursos (R.raw)
        soundMap["jump"] = soundPool.load(context, R.raw.jump, 1)  // Carrega o som de "jump"
        soundMap["death"] = soundPool.load(context, R.raw.death, 1)  // Carrega o som de "death"
        soundMap["score"] = soundPool.load(context, R.raw.score, 1)  // Carrega o som de "score"
    }

    // Função para tocar o som de "pulo"
    fun playJumpSound() {
        playSoundFromMap("jump")  // Chama o método para tocar o som de "jump"
    }

    // Função para tocar o som de "morte"
    fun playDeathSound() {
        playSoundFromMap("death")  // Chama o método para tocar o som de "death"
    }

    // Função para tocar o som de "pontuação"
    fun playScoreSound() {
        playSoundFromMap("score")  // Chama o método para tocar o som de "score"
    }

    // Método privado que toca um som a partir do mapa de sons carregados
    private fun playSoundFromMap(soundName: String) {
        soundMap[soundName]?.let { soundId ->  // Verifica se o som existe no mapa
            // Toca o som usando o SoundPool
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)  // Parametros: id do som, volume esquerdo, volume direito, prioridade, loop, taxa de velocidade
        }
    }

    // Função para liberar os recursos do SoundPool quando não for mais necessário
    fun release() {
        soundPool.release()  // Libera os recursos do SoundPool para evitar vazamento de memória
    }
}
