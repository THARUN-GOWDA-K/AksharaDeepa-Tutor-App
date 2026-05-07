import json
from science_qs import science_extra
from math_qs import math_extra
from social_qs import social_extra

mock_questions = {
    # SCIENCE
    "Chemical Reactions and Equations": [
        ("Which of the following is a chemical change?", "Melting ice", "Rusting of iron", "Boiling water", "Crushing a can", "B", "Rusting involves the formation of a new substance, iron oxide."),
        ("What is the chemical formula for quicklime?", "Ca(OH)2", "CaCO3", "CaO", "CaCl2", "C", "Quicklime is calcium oxide (CaO).")
    ],
    "Acids, Bases and Salts": [
        ("What is the pH of a neutral solution?", "7", "0", "14", "5", "A", "A neutral solution has a pH of exactly 7 at room temperature."),
        ("Which acid is present in sour milk?", "Citric acid", "Lactic acid", "Tartaric acid", "Oxalic acid", "B", "Lactic acid is produced when milk turns sour.")
    ],
    "Metals and Non-metals": [
        ("Which metal is liquid at room temperature?", "Iron", "Gold", "Mercury", "Copper", "C", "Mercury is the only metal that is liquid at standard room temperature."),
        ("Which non-metal is a good conductor of electricity?", "Sulphur", "Graphite", "Phosphorus", "Iodine", "B", "Graphite, an allotrope of carbon, conducts electricity freely.")
    ],
    "Carbon and its Compounds": [
        ("What is the valency of carbon?", "2", "3", "4", "5", "C", "Carbon has 4 valence electrons, allowing it to form 4 covalent bonds."),
        ("Which of these is an allotrope of carbon?", "Diamond", "Ozone", "Silica", "Quartz", "A", "Diamond is a crystalline structure consisting solely of carbon atoms.")
    ],
    "Life Processes": [
        ("Which part of the plant is responsible for transpiration?", "Xylem", "Phloem", "Stomata", "Root hairs", "C", "Stomata are tiny pores on leaves that release water vapor."),
        ("What is the energy currency of the cell?", "DNA", "RNA", "ATP", "ADP", "C", "ATP (Adenosine Triphosphate) provides energy for cellular processes.")
    ],
    "Control and Coordination": [
        ("Which hormone regulates blood sugar levels?", "Insulin", "Adrenaline", "Thyroxine", "Testosterone", "A", "Insulin is produced by the pancreas to regulate blood glucose."),
        ("What is the basic unit of the nervous system?", "Nephron", "Neuron", "Brain", "Spinal cord", "B", "The neuron is the fundamental structural and functional unit of the nervous system.")
    ],
    "How do Organisms Reproduce?": [
        ("Which of the following reproduces through budding?", "Amoeba", "Yeast", "Plasmodium", "Paramoecium", "B", "Yeast reproduces asexually by forming buds."),
        ("What is the male reproductive part of a flower called?", "Carpel", "Stigma", "Stamen", "Ovary", "C", "The stamen consists of the anther and the filament.")
    ],
    "Heredity and Evolution": [
        ("Who is known as the father of genetics?", "Charles Darwin", "Gregor Mendel", "Albert Einstein", "Isaac Newton", "B", "Mendel's work with pea plants established the fundamental laws of inheritance."),
        ("What carries the genetic information in humans?", "Proteins", "Carbohydrates", "DNA", "Lipids", "C", "DNA holds the genetic blueprint for living organisms.")
    ],
    "Light - Reflection and Refraction": [
        ("The focal length of a plane mirror is:", "Zero", "Infinite", "25 cm", "-25 cm", "B", "A plane mirror is perfectly flat, effectively having an infinite radius."),
        ("Which lens is used to correct myopia?", "Convex", "Concave", "Cylindrical", "Bifocal", "B", "Concave lenses diverge light to correct near-sightedness.")
    ],
    "The Human Eye and the Colourful World": [
        ("What causes the blue color of the sky?", "Reflection", "Refraction", "Scattering", "Dispersion", "C", "Rayleigh scattering of sunlight by the atmosphere causes the sky's blue color."),
        ("Which part of the eye controls the amount of light entering?", "Retina", "Cornea", "Pupil", "Iris", "D", "The iris adjusts the pupil size to control light entry.")
    ],
    "Electricity": [
        ("The SI unit of electric current is:", "Volt", "Ohm", "Ampere", "Watt", "C", "Ampere is the SI unit of electric current."),
        ("What is the formula for Ohm's Law?", "V = I/R", "V = IR", "I = VR", "R = VI", "B", "Ohm's Law states Voltage equals Current multiplied by Resistance.")
    ],
    "Magnetic Effects of Electric Current": [
        ("Which rule determines the direction of magnetic field around a straight conductor?", "Fleming's Left Hand Rule", "Right Hand Thumb Rule", "Fleming's Right Hand Rule", "Faraday's Law", "B", "The Right Hand Thumb Rule gives the direction of the magnetic field."),
        ("What device converts electrical energy to mechanical energy?", "Generator", "Motor", "Transformer", "Galvanometer", "B", "An electric motor uses magnetic effects to create motion.")
    ],
    "Our Environment": [
        ("Which of the following is a primary consumer?", "Lion", "Eagle", "Grasshopper", "Snake", "C", "Grasshoppers eat plants (producers), making them primary consumers."),
        ("What is the main cause of ozone layer depletion?", "CO2", "CFCs", "Methane", "Oxygen", "B", "Chlorofluorocarbons (CFCs) break down ozone molecules in the stratosphere.")
    ],
    "Sustainable Management of Natural Resources": [
        ("Which of these is a renewable resource?", "Coal", "Petroleum", "Solar energy", "Natural gas", "C", "Solar energy is constantly replenished by the sun."),
        ("What does the 'R' stand for in the 3 R's of environment?", "Repair, Reuse, Recycle", "Reduce, Reuse, Recycle", "Remake, Reuse, Reduce", "Remove, Reuse, Recycle", "B", "Reduce, Reuse, and Recycle are core to sustainable management.")
    ],
    "Sources of Energy": [
        ("Which is a conventional source of energy?", "Wind", "Solar", "Fossil fuels", "Tidal", "C", "Fossil fuels have been widely used for decades and are conventional."),
        ("Biogas mainly consists of:", "Methane", "Oxygen", "Nitrogen", "Hydrogen", "A", "Biogas is predominantly methane (around 50-75%).")
    ],
    "Periodic Classification of Elements": [
        ("Who proposed the Modern Periodic Law?", "Mendeleev", "Dobereiner", "Newlands", "Moseley", "D", "Henry Moseley established that atomic number is the fundamental property."),
        ("How many groups are in the modern periodic table?", "7", "18", "15", "8", "B", "There are 18 vertical columns called groups.")
    ],

    # MATH
    "Arithmetic Progressions": [
        ("If the common difference of an AP is 5, then what is a_18 - a_13?", "5", "20", "25", "30", "C", "The difference is 5d = 5 * 5 = 25."),
        ("The nth term of an AP is given by:", "a + (n-1)d", "a + nd", "a - (n-1)d", "a / (n-1)d", "A", "The standard formula for the nth term is a + (n-1)d.")
    ],
    "Triangles": [
        ("If a line is drawn parallel to one side of a triangle, it divides the other two sides in:", "Different ratios", "The same ratio", "Equal halves", "None", "B", "This is Thales's Theorem or the Basic Proportionality Theorem."),
        ("Which criterion is NOT used for similarity of triangles?", "AAA", "SAS", "SSS", "RHS", "D", "RHS is a congruence criterion, not a similarity criterion.")
    ],
    "Pair of Linear Equations in Two Variables": [
        ("If two lines intersect at a single point, the pair of equations has:", "No solution", "Unique solution", "Infinite solutions", "Two solutions", "B", "Intersecting lines mean there is exactly one unique solution."),
        ("If a1/a2 = b1/b2 != c1/c2, the lines are:", "Intersecting", "Coincident", "Parallel", "Perpendicular", "C", "This condition means the lines are parallel and have no solution.")
    ],
    "Circles": [
        ("A tangent intersects the circle at how many points?", "1", "2", "0", "Infinite", "A", "A tangent touches the circle at exactly one point."),
        ("The angle between a tangent and the radius at the point of contact is:", "45", "60", "90", "180", "C", "The tangent is always perpendicular to the radius at the point of contact.")
    ],
    "Areas Related to Circles": [
        ("What is the area of a circle with radius r?", "2πr", "πr^2", "2πr^2", "πr", "B", "The standard formula for the area of a circle is πr^2."),
        ("The perimeter of a semicircle is:", "πr", "πr + 2r", "2πr", "πr^2", "B", "It includes the arc (πr) plus the straight diameter (2r).")
    ],
    "Constructions": [
        ("To divide a line segment in the ratio 3:2, how many equal points do you mark on the acute angle line?", "3", "2", "5", "6", "C", "You mark 3 + 2 = 5 points."),
        ("Which instrument is primarily used to draw a circle?", "Ruler", "Protractor", "Compass", "Set square", "C", "A compass is used to draw arcs and circles.")
    ],
    "Coordinate Geometry": [
        ("The distance of a point (x, y) from the origin is:", "x + y", "√(x^2 + y^2)", "x^2 + y^2", "√(x^2 - y^2)", "B", "By the Pythagorean theorem, the distance is √(x^2 + y^2)."),
        ("What is the y-coordinate of any point on the x-axis?", "1", "x", "0", "Infinite", "C", "On the x-axis, the vertical distance is zero, so y=0.")
    ],
    "Real Numbers": [
        ("The decimal expansion of an irrational number is:", "Terminating", "Non-terminating recurring", "Non-terminating non-recurring", "None", "C", "Irrational numbers have non-terminating and non-recurring decimal expansions."),
        ("For any two positive integers a and b, HCF * LCM equals:", "a+b", "a-b", "a/b", "a*b", "D", "The product of two numbers equals the product of their HCF and LCM.")
    ],
    "Polynomials": [
        ("The maximum number of zeroes a quadratic polynomial can have is:", "1", "2", "3", "0", "B", "A polynomial of degree 2 can have at most 2 zeroes."),
        ("The graph of a quadratic polynomial is a:", "Straight line", "Circle", "Parabola", "Ellipse", "C", "Quadratic equations always form a parabolic curve on a graph.")
    ],
    "Quadratic Equations": [
        ("The discriminant of a quadratic equation ax^2 + bx + c = 0 is:", "b^2 + 4ac", "b^2 - 4ac", "b - 4ac", "a^2 - 4bc", "B", "The discriminant is given by the formula b^2 - 4ac."),
        ("If the roots are real and equal, the discriminant is:", "Greater than 0", "Less than 0", "Equal to 0", "1", "C", "When b^2 - 4ac = 0, the equation has two equal real roots.")
    ],
    "Introduction to Trigonometry": [
        ("What is the value of sin(90°)?", "0", "1", "1/2", "Undefined", "B", "The sine of 90 degrees is exactly 1."),
        ("tan(θ) is equal to:", "sin/cos", "cos/sin", "1/sin", "1/cos", "A", "Tangent is the ratio of sine to cosine.")
    ],
    "Some Applications of Trigonometry": [
        ("The line drawn from the eye of an observer to the point in the object viewed is called:", "Line of sight", "Horizontal line", "Angle of elevation", "Angle of depression", "A", "This is the definition of the line of sight."),
        ("If the angle of elevation of the sun is 45°, the length of a shadow is:", "Half the height", "Equal to the height", "Double the height", "Zero", "B", "Since tan(45°) = 1, the height equals the shadow length.")
    ],
    "Statistics": [
        ("The value of an observation that occurs most frequently is called:", "Mean", "Median", "Mode", "Range", "C", "Mode is the most frequently occurring value in a data set."),
        ("The sum of deviations of data values from their mean is:", "Mean", "1", "0", "Variance", "C", "The deviations above and below the mean perfectly cancel each other out.")
    ],
    "Probability": [
        ("The probability of an impossible event is:", "1", "0", "0.5", "-1", "B", "An event that cannot happen has a probability of 0."),
        ("The sum of probabilities of all elementary events of an experiment is:", "0", "1", "0.5", "Infinite", "B", "The total probability of all possible outcomes always equals 1.")
    ],
    "Surface Areas and Volumes": [
        ("The volume of a cylinder is:", "πr^2h", "1/3 πr^2h", "2πrh", "4/3 πr^3", "A", "Volume of a cylinder is base area (πr^2) times height (h)."),
        ("The surface area of a sphere of radius r is:", "2πr^2", "3πr^2", "4πr^2", "πr^2", "C", "A sphere's total surface area is exactly 4πr^2.")
    ],

    # SOCIAL
    "Advent of Europeans to India": [
        ("Who discovered the sea route to India in 1498?", "Christopher Columbus", "Vasco da Gama", "Ferdinand Magellan", "James Cook", "B", "Vasco da Gama reached Calicut in 1498, establishing a sea route."),
        ("Which European power was the first to establish trade in India?", "British", "French", "Dutch", "Portuguese", "D", "The Portuguese were the first to arrive for trade in India.")
    ],
    "The Extension of the British Rule": [
        ("Who introduced the Doctrine of Lapse?", "Lord Wellesley", "Lord Dalhousie", "Lord Cornwallis", "Lord Curzon", "B", "Lord Dalhousie implemented this policy to annex Indian states."),
        ("Which battle established British supremacy in Bengal?", "Battle of Panipat", "Battle of Buxar", "Battle of Plassey", "Battle of Haldighati", "C", "The Battle of Plassey (1757) marked the beginning of British political rule.")
    ],
    "The Impact of British Rule in India": [
        ("Who implemented the Permanent Settlement in Bengal?", "Lord Dalhousie", "Lord Cornwallis", "Lord William Bentinck", "Warren Hastings", "B", "Lord Cornwallis introduced it in 1793 to fix land revenue."),
        ("Which system of education was introduced by the British in 1835?", "Gurukul System", "Macaulay's English Education", "Vernacular Education", "Basic Education", "B", "Thomas Macaulay introduced English as the medium of instruction.")
    ],
    "Opposition to British Rule in Karnataka": [
        ("Who led the rebellion in Kittur against the British?", "Rani Abbakka", "Kittur Chennamma", "Keladi Chennamma", "Onake Obavva", "B", "Rani Chennamma of Kittur fought the British in 1824 against the Doctrine of Lapse."),
        ("Sangolli Rayanna fought alongside which ruler?", "Tipu Sultan", "Hyder Ali", "Kittur Chennamma", "Krishnaraja Wodeyar", "C", "Rayanna was a prominent warrior in Kittur Chennamma's army.")
    ],
    "Social and Religious Reformation Movements": [
        ("Who founded the Brahmo Samaj?", "Swami Vivekananda", "Raja Ram Mohan Roy", "Dayananda Saraswati", "Jyotiba Phule", "B", "Raja Ram Mohan Roy founded it in 1828 to reform Hindu society."),
        ("Who started the Ramakrishna Mission?", "Ramakrishna Paramahamsa", "Swami Vivekananda", "Aurobindo Ghosh", "Rabindranath Tagore", "B", "Swami Vivekananda started it in 1897 to spread his guru's teachings.")
    ],
    "The First War of Indian Independence (1857)": [
        ("Who was the last Mughal Emperor exiled to Rangoon?", "Akbar II", "Shah Alam II", "Bahadur Shah Zafar", "Jahangir", "C", "Bahadur Shah Zafar was exiled to Rangoon by the British after the 1857 revolt."),
        ("What was the immediate cause of the 1857 revolt?", "Doctrine of Lapse", "Heavy Taxation", "Greased Cartridges", "Subsidiary Alliance", "C", "The rumor of cartridges greased with cow and pig fat sparked the mutiny.")
    ],
    "Freedom Struggle": [
        ("When was the Indian National Congress founded?", "1857", "1885", "1905", "1942", "B", "A.O. Hume founded the INC in 1885."),
        ("Who was known as the 'Lion of Punjab'?", "Bhagat Singh", "Lala Lajpat Rai", "Bal Gangadhar Tilak", "Bipin Chandra Pal", "B", "Lala Lajpat Rai was famously known as Punjab Kesari.")
    ],
    "Era of Gandhi and National Movement": [
        ("In which year did the Dandi March take place?", "1920", "1930", "1942", "1919", "B", "The Dandi March (Salt Satyagraha) began on March 12, 1930."),
        ("Which movement was launched in 1942?", "Non-Cooperation Movement", "Civil Disobedience", "Quit India Movement", "Khilafat Movement", "C", "Gandhi launched the Quit India Movement demanding an end to British rule.")
    ],
    "Post-Independent India": [
        ("Who was the first Prime Minister of independent India?", "Sardar Patel", "Rajendra Prasad", "Jawaharlal Nehru", "B.R. Ambedkar", "C", "Nehru took office on August 15, 1947."),
        ("Who is known as the Iron Man of India?", "Subhas Chandra Bose", "Sardar Vallabhbhai Patel", "Bhagat Singh", "Lal Bahadur Shastri", "B", "Patel is credited with integrating the princely states into a united India.")
    ],
    "The Political Developments of 20th Century": [
        ("When did World War I begin?", "1914", "1918", "1939", "1945", "A", "World War I broke out in July 1914."),
        ("Which organization was formed after World War I to maintain peace?", "United Nations", "League of Nations", "NATO", "Warsaw Pact", "B", "The League of Nations was founded in 1920 but failed to prevent WWII.")
    ],
    "India's Relationship with Other Countries": [
        ("The Panchsheel agreement was signed between India and:", "Pakistan", "USA", "Russia", "China", "D", "It was signed in 1954 to establish peaceful coexistence."),
        ("Which line divides India and Pakistan?", "McMahon Line", "Durand Line", "Radcliffe Line", "Maginot Line", "C", "Cyril Radcliffe drew the border line during the partition in 1947.")
    ],
    "Global Problems and India's Role": [
        ("Which issue is a major global environmental problem?", "Deforestation", "Global Warming", "Ozone Depletion", "All of the above", "D", "These are all critical global environmental challenges."),
        ("India has advocated strongly for which type of disarmament?", "Nuclear", "Biological", "Chemical", "Conventional", "A", "India advocates for universal and non-discriminatory nuclear disarmament.")
    ],
    "International Institutions": [
        ("When was the United Nations Organization (UNO) established?", "1919", "1939", "1945", "1950", "C", "The UN was founded on October 24, 1945, after WWII."),
        ("Where is the headquarters of the World Health Organization (WHO)?", "New York", "Geneva", "Paris", "London", "B", "WHO's headquarters is located in Geneva, Switzerland.")
    ],
    "Social Stratification": [
        ("The division of society based on wealth, caste, or education is called:", "Social Mobility", "Social Stratification", "Socialization", "Social Control", "B", "Social stratification is the categorization of people into socio-economic layers."),
        ("Which article of the Indian Constitution abolishes Untouchability?", "Article 14", "Article 15", "Article 17", "Article 19", "C", "Article 17 explicitly abolishes the practice of untouchability.")
    ]
}

# Merge extra questions
for d in [science_extra, math_extra, social_extra]:
    for chapter, qs in d.items():
        if chapter in mock_questions:
            mock_questions[chapter].extend(qs)
        else:
            mock_questions[chapter] = qs

def get_generic_question(subject, ch, index):
    if subject == "SCIENCE":
        return (f"Which concept is central to the study of {ch}?", "Concept W", "Concept X", "Concept Y", "Concept Z", "B", f"Concept X forms the foundation of {ch}.")
    elif subject == "MATH":
        return (f"Calculate the primary value in {ch}:", "1", "2", "3", "4", "A", f"Applying the principles of {ch} yields 1.")
    else:
        return (f"Identify a key aspect of {ch}:", "Event W", "Event X", "Event Y", "Event Z", "A", f"Event W is deeply historical in {ch}.")

out = """package com.aksharadeepa.tutor

import com.aksharadeepa.tutor.data.database.AppDatabase
import com.aksharadeepa.tutor.data.model.Chapter
import com.aksharadeepa.tutor.data.model.QuizQuestion
import com.aksharadeepa.tutor.data.model.StreakData

object MockDataHelper {
    suspend fun prepopulateDb(database: AppDatabase) {
        val chapterDao = database.chapterDao()
        val quizDao = database.quizDao()
        val goalDao = database.goalDao()
        
        if (chapterDao.getChapterCount() > 0) return

        val chapters = mutableListOf<Chapter>()
        val questions = mutableListOf<QuizQuestion>()
"""

chapter_id_counter = 1
question_id_counter = 1

def add_questions(subject, subject_name, chapter_list):
    global chapter_id_counter
    global question_id_counter
    global out
    out += f"        // {subject_name}\n"
    for i, ch in enumerate(chapter_list):
        important_concepts = "Key topics, formulas, historical dates, and important definitions to remember for this chapter."
        if subject == "SCIENCE":
            important_concepts = "Core principles, chemical equations, diagrams, and experiments related to " + ch
        elif subject == "MATH":
            important_concepts = "Theorems, formulas, and step-by-step problem-solving methods for " + ch
        elif subject == "SOCIAL":
            important_concepts = "Key dates, historical figures, geographical maps, and event timelines for " + ch

        out += f'        chapters.add(Chapter(id = {chapter_id_counter}L, subject = "{subject}", chapterName = "{ch}", importantConcepts = "{important_concepts}", orderIndex = {i}))\n'
        
        real_qs = mock_questions.get(ch, [])
        # Provide exactly as many questions as we have for the chapter to avoid generic fallbacks
        # Default to 1 generic question only if there are absolutely no questions
        if not real_qs:
            real_qs = [get_generic_question(subject, ch, 1)]
            
        for qt, oa, ob, oc, od, ans, exp in real_qs:
            qt = qt.replace('"', '\\"')
            exp = exp.replace('"', '\\"')
            out += f'        questions.add(QuizQuestion(id = {question_id_counter}L, chapterId = {chapter_id_counter}L, questionText = "{qt}", optionA = "{oa}", optionB = "{ob}", optionC = "{oc}", optionD = "{od}", correctOption = "{ans}", explanation = "{exp}"))\n'
            question_id_counter += 1
        
        chapter_id_counter += 1

science_chapters = [
    "Chemical Reactions and Equations", "Acids, Bases and Salts", "Metals and Non-metals",
    "Carbon and its Compounds", "Life Processes", "Control and Coordination",
    "How do Organisms Reproduce?", "Heredity and Evolution", "Light - Reflection and Refraction",
    "The Human Eye and the Colourful World", "Electricity", "Magnetic Effects of Electric Current",
    "Our Environment", "Sustainable Management of Natural Resources", "Sources of Energy",
    "Periodic Classification of Elements"
]

math_chapters = [
    "Arithmetic Progressions", "Triangles", "Pair of Linear Equations in Two Variables",
    "Circles", "Areas Related to Circles", "Constructions", "Coordinate Geometry",
    "Real Numbers", "Polynomials", "Quadratic Equations", "Introduction to Trigonometry",
    "Some Applications of Trigonometry", "Statistics", "Probability", "Surface Areas and Volumes"
]

social_chapters = [
    "Advent of Europeans to India", "The Extension of the British Rule", "The Impact of British Rule in India",
    "Opposition to British Rule in Karnataka", "Social and Religious Reformation Movements",
    "The First War of Indian Independence (1857)", "Freedom Struggle", "Era of Gandhi and National Movement",
    "Post-Independent India", "The Political Developments of 20th Century", "India's Relationship with Other Countries",
    "Global Problems and India's Role", "International Institutions", "Social Stratification"
]

add_questions("SCIENCE", "Science", science_chapters)
add_questions("MATH", "Math", math_chapters)
add_questions("SOCIAL", "Social", social_chapters)

out += """
        chapterDao.insertChapters(chapters)
        quizDao.insertQuestions(questions)
        goalDao.updateStreakData(StreakData(currentStreak = 0, lastGoalMetDate = ""))
    }
}
"""

with open("AksharaDeepaTutor/app/src/main/java/com/aksharadeepa/tutor/MockDataHelper.kt", "w", encoding="utf-8") as f:
    f.write(out)

print("Mock data generated successfully.")
