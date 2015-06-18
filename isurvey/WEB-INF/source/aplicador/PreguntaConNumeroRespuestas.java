package aplicador;

public class PreguntaConNumeroRespuestas{
	String pregunta;
	int numeroRespuestas;
	
	public PreguntaConNumeroRespuestas(String pregunta, int numeroRespuestas) {
		super();
		this.pregunta = pregunta;
		this.numeroRespuestas = numeroRespuestas;
	}

	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public int getNumeroRespuestas() {
		return numeroRespuestas;
	}

	public void setNumeroRespuestas(int numeroRespuestas) {
		this.numeroRespuestas = numeroRespuestas;
	}
	
	
}