/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package om.edu.utas.ibri;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import org.apache.lucene.queryparser.classic.ParseException;

public class SearchIndex {

    private static final String INDEX_DIR = "index"; // Replace with your actual index directory

    public static void main(String[] args) throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);

        // Open the Lucene index
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_DIR)));
        IndexSearcher searcher = new IndexSearcher(reader);

        // Standard analyzer for query parsing
        StandardAnalyzer analyzer = new StandardAnalyzer();

        while (true) {
            System.out.print("Enter your search query (or 'q' to quit): ");
            String queryStr = scanner.nextLine();

            if (queryStr.equalsIgnoreCase("q")) {
                break;
            }

            // Parse the query string
            QueryParser parser = new QueryParser("contents", analyzer);
            Query query = parser.parse(queryStr);

            // Search the index and get top 5 results
            ScoreDoc[] hits = searcher.search(query, 5).scoreDocs;

            // Display results
            if (hits.length == 0) {
                System.out.println("No results found.");
            } else {
                System.out.println("Top 5 results:");
                for (int i = 0; i < hits.length && i < 5; i++) {
                    int docId = hits[i].doc;
                    Document doc = searcher.doc(docId);
                    System.out.println(String.format("#%d - %s - %s", i + 1, doc.get("filename"), doc.get("fullpath")));
                }
            }
        }

        // Close the index reader
        reader.close();
    }
}
