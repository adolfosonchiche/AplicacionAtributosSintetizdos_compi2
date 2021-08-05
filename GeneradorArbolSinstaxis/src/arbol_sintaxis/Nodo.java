package arbol_sintaxis;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author hectoradolfo
 */
public class Nodo {

    public static enum Tipo {
        SUMA,
        MULTIPLICACION,
        NUMERO
    }

    private final Tipo tipo;
    private static String datos;
    private Nodo operadorIzq;
    private Nodo operadorDer;
    private int contador = 0;
    /**
     * Valor que almacena el nodo cuando se trata de un numero.
     */
    private int valor;
    private static int correlativo = 1;
   
    private final int id;
    private static final int NUMERO_GRAFICO = 1;

    /**
     * Constructor para los nodos que tienen dos hijos, es decir, los de tipo
     * SUMA, RESTA, MULTIPLICACIÓN Y DIVISION
     *
     * @param operadorIzq operador izquierdo de la operación
     * @param operadorDer operador derecho de la operación
     * @param tipo tipo de operación
     */
    public Nodo(Nodo operadorIzq, Nodo operadorDer, Tipo tipo) {
        this.tipo = tipo;
        this.operadorIzq = operadorIzq;
        this.operadorDer = operadorDer;
        id = correlativo++;
        contador++;
    }

    /**
     * Constructor para los nodos que no tienen hijos, es decir, los de tipo
     * NUMERO
     *
     * @param valor Valor específico del número que almacena el nodo
     */
    public Nodo(int valor) {
        this.tipo = Tipo.NUMERO;
        this.valor = valor;
        id = correlativo++;
    }

    /**
     * Método que retorna el valor de una expresión aritmética a partir del
     * árbol de dicha expresión
     *
     * @return Retorna el resultado de la operación.
     */
    public int getValor() {
        if (null == tipo) {
            datos = datos + " " + valor;
            //si se trata de un número.
            return valor;
        } else switch (tipo) {
            case MULTIPLICACION:
                datos = datos + " * " + valor;
                return operadorIzq.getValor() * operadorDer.getValor();
            case SUMA:
                datos = datos + " + " + valor;
                return operadorIzq.getValor() + operadorDer.getValor();
            default:
                //si se trata de un número.
                return valor;
        }
    }
    
    public void postorden(Nodo a) {
        datos = "";
        contador = correlativo;
        try {
            System.out.println("Recorrido PostOrden del árbol binario de búsqueda:");
        ordenarPostorden(a);
        System.out.println();
        } catch (Exception e) {
            System.out.println("error en postorden " + e);
        }
        
    }

    private void ordenarPostorden(Nodo a) {
        if (a.operadorIzq == null || a.operadorDer == null) {
            datos += a.valor + " ";
             System.out.println(a.valor + "," + a.tipo);
            return ;
        }
        
         //System.out.println(a.valor + "," + a.tipo);
        ordenarPostorden(a.operadorIzq);     
        ordenarPostorden(a.operadorDer);
        if (null == a.tipo) {
            datos += a.valor + " ";
                System.out.println(a.valor + "," + a.tipo);
            
        } else switch (a.tipo) {
            case MULTIPLICACION:
                 datos +=  " * ";
                System.out.println( " * " + a.tipo);
                break;
            case SUMA:
                datos +=  " + ";
                System.out.println( "+ ," + a.tipo);
                break;
            default:
                //si se trata de un número.
                datos += a.valor + " ";
                System.out.println(a.valor + "," + a.tipo);
                break;
        }
        /*datos += a.valor.toString() + ",";
        System.out.println(a.valor + "," + a.tipo);*/
    }

    /**
     * Método que genera el gráfico de la expresión aritmética con graphviz,
     * considerando como la raíz de dicho árbol el actual Nodo.
     *
     * @return El nombre del archivo en el que se guardó la imagen generada.
     */
    public String graficar() {
        FileWriter fichero = null;
        PrintWriter escritor;
        try {
            fichero = new FileWriter("graf" + (NUMERO_GRAFICO) + ".dot");
            escritor = new PrintWriter(fichero);
            escritor.print(getCodigoGraphviz());
        } catch (Exception e) {
            System.err.println("Error al escribir el archivo graf" + NUMERO_GRAFICO + ".dot");
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                System.err.println("Error al cerrar el archivo graf" + NUMERO_GRAFICO + ".dot");
            }
        }
        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec("dot -Tjpg -o graf" + NUMERO_GRAFICO + ".jpg graf" + NUMERO_GRAFICO + ".dot");
        } catch (Exception ex) {
            System.err.println("Error al generar la imagen para el archivo "
                    + "graf" + NUMERO_GRAFICO + ".dot");
        }
        return "graf" + NUMERO_GRAFICO + ".jpg";
    }

    /**
     * Método que retornoa el código que grapviz usará para generar la imagen de
     * la expresión aritmética evaluada.
     *
     * @return
     */
    private String getCodigoGraphviz() {
        return "digraph grafica{\n"
                + "rankdir=TB;\n"
                + "node [shape = record, style=filled, fillcolor=seashell2];\n"
                + getCodigoInterno()
                + "}\n";
    }

    /**
     * Genera el código interior de graphviz, este método tiene la
     * particularidad de ser recursivo, esto porque recorrer un árbol de forma
     * recursiva es bastante sencillo y reduce el código considerablemente.
     *
     * @return
     */
    private String getCodigoInterno() {
        String etiqueta;
         if (null == tipo) {
             //si se trata de un número.
             return "nodo" + id + " [ label =\"" + valor + "\"];\n";
             
         }  else switch (tipo) {
            case MULTIPLICACION:
                etiqueta = "nodo" + id + " [ label =\"<C0>|*|<C1>\"];\n";
                break;
            case SUMA:
                etiqueta = "nodo" + id + " [ label =\"<C0>|+|<C1>\"];\n";
                break;
            default:
                //si se trata de un número.
                return "nodo" + id + " [ label =\"" + valor + "\"];\n";
        }
        return etiqueta
                + operadorIzq.getCodigoInterno()
                + "nodo" + id + ":C0->nodo" + operadorIzq.id + "\n"
                + operadorDer.getCodigoInterno()
                + "nodo" + id + ":C1->nodo" + operadorDer.id + "\n";
    }
    
    public String getDatos(){
        return datos;
    }
    
    

}
