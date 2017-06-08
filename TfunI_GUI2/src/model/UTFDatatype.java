package model;

/**
 * Datentyp welcher eine �bertragungsfunktion representiert. Diese besteht aus
 * einem Bruch mit einer Konstanten ({@link #zaehler})in Z�hler und einem
 * Polynom im Nenner. Das Attribut Ordnung definert die Ordnung des
 * Nennerpolynoms. Mit den Attributen {@link #sigma} und
 * {@link UTFDatatype#koeffWQ} werden die Polstellen der �bertragungsfunktion
 * definiert.
 * 
 * @author Team 1
 *
 */
public class UTFDatatype {

	public double zaehler;
	public double[] koeffWQ;
	public double sigma;
	public int ordnung;

}
