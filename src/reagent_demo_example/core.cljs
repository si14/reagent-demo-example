(ns reagent-demo-example.core
  (:require
   [reagent.core :as r]
   [clojure.string :as str]
   [cljs.spec :as s]))

;(defonce timer (r/atom (js/Date.)))
;
;(defonce time-color (r/atom "#666"))
;
;(defonce time-updater (js/setInterval
;                        #(reset! timer (js/Date.)) 1000))
;
;(defn greeting [message]
;  [:h1 message])
;
;(defn clock []
;  (let [time-str (-> @timer .toTimeString (clojure.string/split " ") first)]
;    [:div.example-clock
;     {:style {:color @time-color}}
;     time-str]))
;
;(defn color-input []
;  [:div.color-input
;   "Time color: "
;   [:input {:type "text"
;            :value @time-color
;            :on-change #(reset! time-color (-> % .-target .-value))}]])
;
;(defn simple-example []
;  [:div
;   [greeting "Hello world, it is now"]
;   [clock]
;   [color-input]])
;
;(r/render-component [simple-example]
;                          (. js/document (getElementById "app")))


(s/def ::id integer?)
(s/def ::name string?)
(s/def ::organism string?)
(s/def ::method string?)
(s/def ::platform string?)
(s/def ::experiment (s/keys :req-un [::id ::name ::organism ::method ::platform]))
(s/def ::experiments (s/map-of integer? ::experiment))

(defonce counter (r/atom 0))

(defonce experiments (r/atom (sorted-map)))

(add-watch experiments ::demo-watch
           (fn [_ _ _ new]
             (when-not (s/valid? ::experiments new)
               (.log js/console "YOLO" (s/explain-str ::experiments new)))))

(def columns ["Name" "Organism" "Method" "Platform"])

(def test-data [
                ["The Path to Triacylglyceride Obesity in the sta6 Strain of Chlamydomonas reinhardtii" "Chlamydomonas reinhardtii" "" "Illumina HiSeq 2000"]
                ["Modelling and rescuing neurodevelopmental defect of Down syndrome using induced pluripotent stem cells from monozygotic twins discordant for trisomy 21 [GEO: GSE52249]" "Homo sapiens" "RNA-Seq" "Illumina HiSeq 2000"]
                ["Let-7 represses Nr6a1 and a mid-gestation developmental program in adult fibroblasts [mRNA-seq_Flag-HA-NR6A1_overexpr]" "Mus musculus" "" "Illumina HiSeq 2000"]
                ["RNA-seq from paired human CRC/control mucosa samples" "Homo sapiens" "llumina HiSeq 2000" ""]
                ["Control of Pro-Inflammatory Gene Programs by Regulated Trimethylation and Demethylation of Histone H4K20" "Mus musculus" "" "Illumina Genome Analyzer II"]
                ["Four distinct types of dehydration stress memory genes in Arabidopsis thaliana" "Arabidopsis thaliana" "" "Illumina Genome Analyzer II"]
                ["Global analysis of p53-regulated transcription reveals its direct targets and unexpected regulatory mechanisms (GRO-Seq)" "Homo sapiens" "" "Illumina HiSeq 2000"]
                ["tRNAs marked with CCACCA are targeted for degradatio" "Glycine max" "" "Illumina HiSeq 2000"]
                ["Characterization of the single-cell transcriptional landscape by highly multiplex RNA-Seq" "Mus musculus" "RNA-Seq" ""]
                ["Transcription factors OVOL1 and OVOL2 induce the mesenchymal to epithelial transition in human cancer" "Homo sapiens" "" "Illumina HiSeq 2000"]
                ["Global small RNA analysis in fast-growing Arabidposis thaliana with elevated level of ATP and sugars" "Arabidopsis thaliana" "" "Illumina HiSeq 2000"]
                ["Functional non-canonical microRNAs in the mammalian hippocampus and cortex" "Mus musculus" "" "Illumina Genome Analyzer II"]
                ["Impact of library preparation on downstream analysis and interpretation of RNA-seq data: comparison between Illumina PolyA and NuGEN Ovation protocol" "Homo sapiens" "" "Illumina Genome Analyzer IIx, Illumina HiSeq 2000"]
                ["In vivo and transcriptome-wide identification of RNA-binding protein target sites [RNA-Seq]" "Caenorhabditis elegans" "" "Illumina HiSeq 2000"]
                ["Gene expression analysis of murine SAMHD1 deficient peritoneal macrophages (S1056 to S1075" "Mus musculus" "" "Illumina HiSeq 2000"]
                ["Let-7 represses Nr6a1 and a mid-gestation developmental program in adult fibroblasts [RNA-seq_siRNA_transfection]" "Mus musculus" "" "Illumina HiSeq 2000"]
                ["Function, targets and evolution of Caenorhabditis elegans piRNAs (small RNA-Seq)" "Caenorhabditis elegans" "" "Illumina Genome Analyzer II"]
                ["Small RNA expression in swi6 mutants or dcr1 delta fission yeast Schizosaccharomyces pombe" "Schizosaccharomyces pombe" "" "Illumina HiSeq 2000"]
                ["UBL5 is essential for pre-mRNA splicing and sister chromatid cohesion in human cells" "Homo sapiens" "" "Illumina HiSeq 2000"]
                ["Identification of soybean seed developmental stage specific and tissue specific miRNA targets by degradome sequencing" "Glycine max" "" 2.3]])

(defn add-experiment [experiment-data]
  (let [[name organism method platform] experiment-data]
    (let [id (swap! counter inc)]
      (.log js/console id)
      (swap! experiments assoc id {:id id :name name :organism organism :method method :platform platform}))))

(defonce init (do
                (doseq [experiment test-data] (add-experiment experiment))))

(defn experiment-table-head []
  [:thead
   [:tr
    (for [[idx column] (map-indexed vector columns)]
      ^{:key idx}
      [:td
       [:b column]])]])

(defn experiment-row [experiment]
  [:tr {:on-click #(js/alert experiment)}
   (for [[idx column] (map-indexed vector columns)]
     ^{:key idx}
     [:td (get experiment (keyword (str/lower-case column)))])])

(defn table-body []
  (let [experiments (vals @experiments)]
    [:tbody
     (for [[idx experiment] (map-indexed vector experiments)]
       ^{:key idx}
       [experiment-row experiment])]))

(defn experiment-table []
  [:table {:class "table table-hover experiment-table"}
   [experiment-table-head]
   [table-body]])

(r/render-component [experiment-table]
                    (. js/document (getElementById "app")))
