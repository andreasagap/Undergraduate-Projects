; Sun Dec 31 16:59:56 EET 2017
; 
;+ (version "3.5")
;+ (build "Build 663")
;+ ANDREAS AGAPITOS AEM 2530
;+ PANTELIS KYRIAKIDIS AEM 2551
;+================CLASSES================================================
(defclass %3ACLIPS_TOP_LEVEL_SLOT_CLASS "Fake class to save top-level slot information"
    (is-a USER)
    (role abstract)
    (multislot PreviousManholes
        (type INSTANCE)
        (allowed-classes Manhole)
        (cardinality 1 ?VARIABLE)
        (create-accessor read-write))
    (slot Colour
        (type SYMBOL)
        (default none)
        (create-accessor read-write))
    (slot PH
        (type FLOAT)
        (range 0.0 14.0)
        (create-accessor read-write))
    (multislot Spectroscopy
        (type SYMBOL)
        (default none)
        (create-accessor read-write))
    (multislot previous
        (type INSTANCE)
        (allowed-classes Manhole Warehouse)
        (cardinality 1 ?VARIABLE)
        (create-accessor read-write))
    (slot Name_ch
        (type STRING)
        (create-accessor read-write))
    (multislot next
        (type INSTANCE)
        (allowed-classes Manhole StationControl)
        (create-accessor read-write))
    (slot Specific_gravity
        (type FLOAT)
        (range 0.9 1.1)
        (default 1.0)
        (create-accessor read-write))
    (multislot Attribute
        (type STRING)
        (default "none")
        (create-accessor read-write))
    (slot corrosive
        (type SYMBOL)
        (allowed-values FALSE TRUE)
        (create-accessor read-write))
    (slot Radioactivity
        (type SYMBOL)
        (allowed-values no yes)
        (default no)
        (create-accessor read-write))
    (slot Solubility
        (type SYMBOL)
        (allowed-values yes no)
        (create-accessor read-write))
    (slot Smell
        (type SYMBOL)
        (default none)
        (create-accessor read-write))
    (multislot Elements
        (type INSTANCE)
        (allowed-classes StrongAcid StrongBase Oil WeakBase WeakAcid)
        (cardinality 1 ?VARIABLE)
        (create-accessor read-write))
)
(defclass Chemical_Element
    (is-a USER)
    (role concrete) (pattern-match reactive)
    (slot Colour
        (type SYMBOL)
        (default none)
        (create-accessor read-write))
	(multislot Attribute
        (type STRING)
        (default "none")
        (create-accessor read-write))
    (slot Smell
        (type SYMBOL)
        (default none)
        (create-accessor read-write))
    (slot PH
        (type FLOAT)
        (range 0.0 14.0)
        (create-accessor read-write))
    (multislot Spectroscopy
        (type SYMBOL)
        (default none)
        (create-accessor read-write))
    (slot Radioactivity
        (type SYMBOL)
        (allowed-values yes no)
        (default no)
        (create-accessor read-write))
    (slot Solubility
        (type SYMBOL)
        (allowed-values yes no)
        (create-accessor read-write))
    (slot Specific_gravity
        (type FLOAT)
        (range 0.9 1.1)
        (default 1.0)
        (create-accessor read-write))
    (slot Name_ch
        (type STRING)
        (create-accessor read-write))
    (slot candidate (type SYMBOL) (allowed-symbols yes no) (default yes))
)
(defclass Oil
    (is-a Chemical_Element)
    (role concrete)
    (slot PH
        (type FLOAT)
        (range 6.0 7.99)
        (create-accessor read-write))
    (slot Solubility
        (type SYMBOL)
        (allowed-values yes no)
        (default no)
        (create-accessor read-write)))
(defclass Base
    (is-a Chemical_Element)
    (role concrete)
    (slot PH
        (type FLOAT)
        (range 8.0 14.0)
        (create-accessor read-write))
    (slot corrosive
        (type SYMBOL)
        (allowed-values no yes)
        (create-accessor read-write))
    (slot Solubility
        (type SYMBOL)
        (allowed-values yes no)
        (default yes)
        (create-accessor read-write)))
(defclass StrongBase
    (is-a Base)
    (role concrete)
    (slot PH
        (type FLOAT)
        (range 11.0 14.0)
        (create-accessor read-write))
    (slot corrosive
        (type SYMBOL)
        (allowed-values FALSE TRUE)
        (default TRUE)
        (create-accessor read-write)))
(defclass WeakBase
    (is-a Base)
    (role concrete)
    (slot PH
        (type FLOAT)
        (range 8.0 10.999)
        (create-accessor read-write))
    (slot corrosive
        (type SYMBOL)
        (allowed-values FALSE TRUE)
        (default FALSE)
        (create-accessor read-write)))
(defclass Acid
    (is-a Chemical_Element)
    (role concrete)
    (slot PH
        (type FLOAT)
        (range 0.0 5.9)
        (create-accessor read-write))
    (slot corrosive
        (type SYMBOL)
        (allowed-values FALSE TRUE)
        (create-accessor read-write))
    (slot Solubility
        (type SYMBOL)
        (allowed-values yes no)
        (default yes)
        (create-accessor read-write)))
(defclass StrongAcid
    (is-a Acid)
    (role concrete) (pattern-match reactive)
    (slot PH
        (type FLOAT)
        (range 0.0 2.9)
        (create-accessor read-write))
    (slot corrosive
        (type SYMBOL)
        (allowed-values FALSE TRUE)
        (default TRUE)
        (create-accessor read-write)))
(defclass WeakAcid
    (is-a Acid) (pattern-match reactive)
    (role concrete)
    (slot PH
        (type FLOAT)
        (range 3.0 5.9)
        (create-accessor read-write))
    (slot corrosive
        (type SYMBOL)
        (allowed-values FALSE TRUE)
        (default FALSE)
        (create-accessor read-write)))
(defclass Warehouse
    (is-a USER)
    (role concrete) (pattern-match reactive)
    (multislot Elements
        (type INSTANCE)
        (allowed-classes StrongAcid StrongBase Oil WeakBase WeakAcid)
        (cardinality 1 ?VARIABLE)
        (create-accessor read-write))
    (slot Name_ch
        (type STRING)
        (create-accessor read-write))
    (slot candidate (type SYMBOL) (allowed-symbols yes no) (default yes)))
(defclass Manhole
    (is-a USER)
    (role concrete) (pattern-match reactive)
    (multislot next
        (type INSTANCE)
        (allowed-classes Manhole StationControl)
        (create-accessor read-write))
    (multislot previous
        (type INSTANCE)
        (allowed-classes Manhole Warehouse)
        (cardinality 1 ?VARIABLE)
        (create-accessor read-write))
    (slot Name_ch
        (type STRING)
        (create-accessor read-write))
    (slot candidate (type SYMBOL) (allowed-symbols yes no) (default yes)))
(defclass StationControl
    (is-a USER)
    (role concrete) (pattern-match reactive)
    (multislot PreviousManholes
        (type INSTANCE)
        (allowed-classes Manhole)
        (cardinality 1 ?VARIABLE)
        (create-accessor read-write)))
;+==================INSTANCES=========================================
(definstances facts
([project_Class0] of  Manhole
    (Name_ch "Manhole_2")
    (next [project_Class30008])
    (previous [project_Class10016]))
([project_Class1] of  StrongAcid
    (Attribute "burn skin")
    (Colour "none")
    (corrosive TRUE)
    (Name_ch "Sulphuric acid")
    (Radioactivity no)
    (Smell none)
    (Solubility yes)
    (Specific_gravity 1.0)
    (Spectroscopy sulphur))
([project_Class10000] of  Warehouse
    (Elements
        [project_Class10002]
        [project_Class1])
    (Name_ch "Warehouse_7"))
([project_Class10001] of  Warehouse
    (Elements
        [project_Class10004]
        [project_Class10003]
        [project_Class10010])
    (Name_ch "Warehouse_4"))
([project_Class10002] of  StrongAcid
    (Attribute
        "burn skin"
        "asphyxiation")
    (Colour none)
    (corrosive TRUE)
    (Name_ch "Hydrochloric acid")
    (Radioactivity no)
    (Smell Choking)
    (Solubility yes)
    (Specific_gravity 1.0)
    (Spectroscopy none))
([project_Class10003] of  WeakAcid
    (Colour none)
    (Name_ch "Acetic acid")
    (Radioactivity yes)
    (Smell Vinegar)
    (Solubility yes)
    (Specific_gravity 1.0)
    (Spectroscopy none))
([project_Class10004] of  WeakAcid
    (Colour none)
    (Name_ch "Carbonic acid")
    (Radioactivity no)
    (Smell none)
    (Solubility yes)
    (Specific_gravity 1.0)
    (Spectroscopy carbon))
([project_Class10005] of  StrongBase
    (Colour none)
    (corrosive TRUE)
    (Name_ch "Sodium hydroxide")
    (Radioactivity no)
    (Smell none)
    (Solubility yes)
    (Specific_gravity 1.0)
    (Spectroscopy sodium))
([project_Class10007] of  WeakBase
    (Colour red)
    (corrosive FALSE)
    (Name_ch "Chromogen 23")
    (Radioactivity no)
    (Smell none)
    (Solubility yes)
    (Specific_gravity 0.9)
    (Spectroscopy none))
([project_Class10008] of  WeakBase
    (Colour white)
    (corrosive FALSE)
    (Name_ch "Aluminium hydroxide")
    (Radioactivity no)
    (Smell none)
    (Solubility yes)
    (Specific_gravity 1.02)
    (Spectroscopy metal))
([project_Class10009] of  WeakBase
    (Colour none)
    (corrosive FALSE)
    (Name_ch "Rubidium hydroxide")
    (Radioactivity yes)
    (Smell none)
    (Solubility yes)
    (Specific_gravity 1.02)
    (Spectroscopy metal))
([project_Class10010] of  Oil
    (Attribute "explosive")
    (Colour none)
    (Name_ch "Petrol")
    (Radioactivity yes)
    (Smell none)
    (Solubility no)
    (Specific_gravity 0.9)
    (Spectroscopy carbon))
([project_Class10011] of  Oil
    (Attribute "highly toxic PCBs")
    (Colour none)
    (Name_ch "Transformer oil")
    (Radioactivity FALSE)
    (Smell none)
    (Solubility no)
    (Specific_gravity 1.0)
    (Spectroscopy "carbon"))
([project_Class10014] of  Manhole
    (Name_ch "Manhole_1")
    (next [project_Class30008])
    (previous [project_Class10015]))
([project_Class10015] of  Warehouse
    (Elements
        [project_Class1]
        [project_Class10010])
    (Name_ch "Warehouse_1"))
([project_Class10016] of  Warehouse
    (Elements
        [project_Class10002]
        [project_Class10003])
    (Name_ch "Warehouse_2"))
([project_Class10017] of  Warehouse
    (Elements
        [project_Class10009]
        [project_Class10011])
    (Name_ch "Warehouse_3"))
([project_Class20001] of  Warehouse
    (Elements
        [project_Class10003]
        [project_Class10004]
        [project_Class10005])
    (Name_ch "Warehouse_8"))
([project_Class20002] of  Warehouse
    (Elements
        [project_Class10007]
        [project_Class1]
        [project_Class10010])
    (Name_ch "Warehouse_5"))
([project_Class20003] of  Warehouse
    (Elements
        [project_Class10008]
        [project_Class10011]
        [project_Class10004])
    (Name_ch "Warehouse_6"))
([project_Class30002] of  Manhole
    (Name_ch "Manhole_3")
    (next [project_Class30008])
    (previous [project_Class10017]))
([project_Class30003] of  Manhole
    (Name_ch "Manhole_4")
    (next [project_Class30009])
    (previous [project_Class10001]))
([project_Class30004] of  Manhole
    (Name_ch "Manhole_5")
    (next [project_Class30009])
    (previous [project_Class20002]))
([project_Class30005] of  Manhole
    (Name_ch "Manhole_6")
    (next [project_Class30010])
    (previous [project_Class20003]))
([project_Class30006] of  Manhole
    (Name_ch "Manhole_7")
    (next [project_Class30010])
    (previous [project_Class10000]))
([project_Class30007] of  Manhole
    (Name_ch "Manhole_8")
    (next [project_Class30012])
    (previous [project_Class20001]))
([project_Class30008] of  Manhole
    (Name_ch "Manhole_9")
    (next [project_Class30011])
    (previous
        [project_Class10014]
        [project_Class0]
        [project_Class30002]))
([project_Class30009] of  Manhole
    (Name_ch "Manhole_10")
    (next [project_Class30011])
    (previous
        [project_Class30003]
        [project_Class30004]))
([project_Class30010] of  Manhole
    (Name_ch "Manhole_11")
    (next [project_Class30012])
    (previous
        [project_Class30005]
        [project_Class30006]))
([project_Class30011] of  Manhole
    (Name_ch "Manhole_12")
    (next [project_Class30015])
    (previous
        [project_Class30009]
        [project_Class30008]))
([project_Class30012] of  Manhole
    (Name_ch "Manhole_13")
    (next [project_Class30015])
    (previous
        [project_Class30010]
        [project_Class30007]))
([project_Class30015] of  StationControl
    (PreviousManholes
        [project_Class30011]
        [project_Class30012]))
)
;+====================FUNCTIONS=========================================
(deffunction ask-question (?question $?allowed-values)
    (printout t ?question)
   (bind ?answer (read))
   (if (lexemep ?answer) 
       then (bind ?answer (lowcase ?answer)))
   (while (not (member ?answer ?allowed-values)) do
      (printout t ?question)
      (bind ?answer (read))
      (if (lexemep ?answer) 
         then (bind ?answer (lowcase ?answer))))
   (return ?answer)
)

;+ =============================RULES=====================================
;+ ------MAIN---------
(defrule asking_for_metrics
    "main"
    (initial-fact)
=>

(printout t "Gia poies metriseis tha dothoyn times? (pH solubility spectroscopy colour smell specific_gravity radioactivity): "  )
(bind $?type (explode$ (readline)))
(assert(list $?type))
(bind ?count (length $?type))

(while (> ?count 0)
    (bind ?v (implode$ (first$ $?type)))
    (if (eq "pH" ?v) 
        then 
            (printout t "Poso einai to pH (0-14): ")
            (bind ?ph (read))
            (assert(pH ?ph))        
    )
    (if (eq "solubility" ?v) 
        then 
            (bind ?soluble (ask-question "Einai soluble (yes,no): " yes no))
            (assert(soluble ?soluble))
    ) 
    (if (eq "spectroscopy" ?v) 
        then 
            (printout t "Dwse Spectroscopy (none,carbon,sulphur,metal,sodium): ")
            (bind $?spectroscopy (explode$ (readline)))
            (assert(spectroscopy $?spectroscopy ))  
        
    )
    (if (eq "colour" ?v) 
        then 
            (bind ?colour (ask-question "Dwse colour (none,white,red):" none white red))
            (assert(colour ?colour))    
        
    )
    (if (eq "smell" ?v) 
        then 
            (bind ?smell (ask-question "Dwse smell (none,Choking,Vinegar): " none Choking Vinegar))
            (assert(smell ?smell))  
        
    )
    (if (eq "specific_gravity" ?v) 
        then 
            (printout t "Dwse specific Gravity [0.9,1.1]: ")
            (bind ?sg (read))
            (assert(sgravity ?sg))
        
    )
    (if (eq "radioactivity" ?v) 
        then 
            (bind ?radio (ask-question "Dwse radioactivity (yes,no): " yes no))
            (assert(radioactivity ?radio))  
        
    )
    (bind ?count (- ?count 1))
    (bind $?type (delete$ $?type 1 1))
)
(assert(warehouse looking_for))
)


(defrule isStrongAcid
    (object (is-a StrongAcid)    ;+ getting Acids
    (name ?x) (candidate yes))
    (pH ?metric_ph)
    (test (> ?metric_ph (nth$ 2 (slot-range StrongAcid PH))))            
    
    =>   
    (modify-instance ?x (candidate no))

)
(defrule isWeakAcid
    
    (object (is-a WeakAcid)   ;+ getting Acids
    
    (name ?x) (candidate yes))
    (pH ?metric_ph)
    (test (or (< ?metric_ph (nth$ 1 (slot-range WeakAcid PH))) (> ?metric_ph (nth$ 2  (slot-range WeakAcid PH)))))            
    =>   
    (modify-instance ?x (candidate no))
)
(defrule isStrongBase
    (object (is-a StrongBase)    ;+ getting StrongBase
    (name ?x) (candidate yes))
    (pH ?metric_ph)
    (test (or (< ?metric_ph (nth$ 1 (slot-range StrongBase PH))) (> ?metric_ph (nth$ 2  (slot-range StrongBase PH)))))                      
    
    =>   
    (modify-instance ?x (candidate no))
)
(defrule isWeakBase
    
    (object (is-a WeakBase)   ;+ getting WeakBase
    
    (name ?x) (candidate yes))
    (pH ?metric_ph)
    (test (or (< ?metric_ph (nth$ 1 (slot-range WeakBase PH))) (> ?metric_ph (nth$ 2  (slot-range WeakBase PH)))))            
    =>   
    (modify-instance ?x (candidate no))
)
(defrule isOil
    
    (object (is-a Oil)   ;+ getting Oil
    
    (name ?x) (candidate yes))
    (pH ?metric_ph)
    (test (or (< ?metric_ph (nth$ 1 (slot-range Oil PH))) (> ?metric_ph (nth$ 2  (slot-range Oil PH)))))            
    =>   
    (modify-instance ?x (candidate no))
)
(defrule checkSoluble
    (object (is-a Chemical_Element)    ;+ getting Element
    (name ?x) (candidate yes))
    (soluble ?s)
    (test (neq ?s (send ?x get-Solubility)))           
    
    =>   
    (modify-instance ?x (candidate no))
)
(defrule checkSmell
    (object (is-a Chemical_Element)    ;+ getting Element
    (name ?x) (candidate yes))

    (smell ?s)
    (test (neq ?s (send ?x get-Smell)))            
    
    =>    

    (modify-instance ?x (candidate no))
)


(defrule checkSpGravity
    (object (is-a Chemical_Element)    ;+ getting Element
    (name ?x) (candidate yes))

    (sgravity ?s)
    (test (neq ?s (send ?x get-Specific_gravity)))            
    
    =>    

    (modify-instance ?x (candidate no))
)
(defrule checkColour
    (object (is-a Chemical_Element)    ;+ getting Element
    (name ?x) (candidate yes))
    (colour ?s)
    (test (neq ?s (send ?x get-Colour)))           
    
    =>   
    (modify-instance ?x (candidate no))
)

(defrule checkRadio
    (object (is-a Chemical_Element)    ;+ getting Element
    (name ?x) (candidate yes))
    (radioactivity ?s)
    (test (neq ?s (send ?x get-Radioactivity)))           
    
    =>   
    (modify-instance ?x (candidate no))
)
(defrule checkSpectro
    (object (is-a Chemical_Element)    ;+ getting Element
    (name ?x) (candidate yes))
    (spectroscopy ?s)
    (test (neq (str-cat ?s) (implode$ (send ?x get-Spectroscopy))))           
    
    =>   
    (modify-instance ?x (candidate no))

)

(defrule checkWarehouse
	(declare (salience -10))
	(object (is-a Warehouse)
	(name ?x) (candidate yes) (Elements $?list))
	(warehouse ?w)
=>	
	(bind ?count (length $?list))
	(bind ?flag no)
	(while (> ?count 0)
		(bind ?v (instance-address *(nth$ 1 ?list)))
		
		(if(eq (send ?v get-candidate) yes)
			then
			(bind ?flag yes)
		)
		(bind ?count (- ?count 1))
    		(bind $?list (delete$ $?list 1 1))
	)
	(if (eq ?flag no) then 	
		(modify-instance ?x (candidate no))
		
	)
	;+(printout t (send ?x get-Name_ch) (send ?x get-candidate) crlf)
	(assert(check manhole))
)


(defrule checkManhole
	(declare (salience -11))
	(object (is-a Manhole)
	(name ?x) (candidate yes) (previous $?list))
	(check ?m)
=>	
	(bind ?count (length $?list))
	(bind ?flag no)
	(while (> ?count 0)
		(bind ?v (instance-address *(nth$ 1 ?list)))
		
		(if(eq (send ?v get-candidate) yes)
			then
			(bind ?flag yes)
		)
		(bind ?count (- ?count 1))
    		(bind $?list (delete$ $?list 1 1))
	)
	(if (eq ?flag no) then 	
		(modify-instance ?x (candidate no))
		
	)
	(assert(station start))
)
(defrule StartStation
	(declare (salience -12))
	(object (is-a StationControl)
	(name ?x)  (PreviousManholes $?list))
	(station ?m)
=>	
	(bind $?candidatesManholes (create$ ))
	(bind ?count (length $?list))
	(bind ?flag no)
	(foreach ?r $?list
		(if(eq (send ?r get-candidate) yes)
			then
			(bind $?candidatesManholes (insert$ $?candidatesManholes 1 ?r))
		)
	)

	(if (> (length $?candidatesManholes) 1) then
		(bind ?answer (ask-question (str-cat "Yparxei molynsi sto "(send (instance-address *(nth$ 1 ?candidatesManholes)) get-Name_ch)" (yes,no):") yes no))
		(modify-instance (instance-address *(nth$ 1 ?candidatesManholes)) 
		(candidate ?answer))
		(foreach ?r $?list
			(if(neq (send ?r get-Name_ch) (send (instance-address *(nth$ 1 ?candidatesManholes)) get-Name_ch))
				then
				(if(eq ?answer yes) then 
					(modify-instance ?r (candidate no))
					else 
					(modify-instance ?r (candidate yes))
				)
			)
		)
		
	)
	(foreach ?r $?list
		(if(eq (send ?r get-candidate) yes)
			then
			(assert(current-position ?r))
		)
	)
)
(defrule FindPollution
	?fact1 <- (current-position ?x)
=>
	(if (eq (class ?x) Manhole) then 
		(bind $?list (send ?x get-previous))
		(if (> (length $?list) 1) then
			(bind $?candidatesManholes (create$ ))
			(foreach ?r $?list
				(if(eq (send ?r get-candidate) yes)
					then
					(bind $?candidatesManholes (insert$ $?candidatesManholes 1 ?r))
				)
			)
			(bind ?count (length $?candidatesManholes))
			(while (> ?count 1) 
				(bind ?answer (ask-question (str-cat "Yparxei molynsi sto "(send (instance-address *(nth$ 1 ?candidatesManholes)) get-Name_ch)" (yes,no):") yes no))
				(modify-instance (instance-address *(nth$ 1 ?candidatesManholes)) (candidate ?answer))
				(foreach ?r $?candidatesManholes
					(if(neq (send ?r get-Name_ch) (send (instance-address *(nth$ 1 ?candidatesManholes)) get-Name_ch))
						then
						(if(eq ?answer yes) then 
							(modify-instance ?r (candidate no))
							
						)
					)
				)
				(if (eq (send (instance-address *(nth$ 1 $?candidatesManholes)) get-candidate) no) then (bind $?candidatesManholes (delete$ $?candidatesManholes 1 1) ))

				(bind ?count 0)
				(foreach ?r $?candidatesManholes
					(if(eq (send ?r get-candidate) yes)
						then
						(bind ?count (+ ?count 1))
					)
				)
			)
			(retract ?fact1)
			(foreach ?r $?candidatesManholes
				(if(eq (send ?r get-candidate) yes)
					then
					(assert(current-position ?r))
				)
			)
			else
				(retract ?fact1)
				(foreach ?r $?list
					(if(eq (send ?r get-candidate) yes)
						then
						(assert(current-position ?r))
					)
				)
		)
		
		else
		(printout t "H phgh ths molynshs einai h apothiki: " (send ?x get-Name_ch) crlf)
		(retract ?fact1)
		(assert (FindElementsInWarehouse ?x))
	)	
	
)

(defrule FindTheElementInWarehouse
	(FindElementsInWarehouse ?x)
=>
	(bind $?list (send ?x get-Elements))
	(foreach ?r $?list
		(if(eq (send ?r get-candidate) yes)
			then
			(printout t "To xhmiko poy prokalese th molynsh pithanon na einai to " (send ?r get-Name_ch) crlf)
			(bind $?attributes (send ?r get-Attribute))
			(if(neq (nth$ 1 $?attributes) "none")
					then
					(printout t "Pithanoi kindynoi: " (implode$ $?attributes) crlf crlf)
			)
		)
	)
)

























