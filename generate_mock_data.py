import json
import os
import random

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

mock_questions = {
    "Chemical Reactions and Equations": [
        ("Which of the following is a chemical change?", "Melting ice", "Rusting of iron", "Boiling water", "Crushing a can", "B", "Rusting involves the formation of a new substance, iron oxide."),
        ("What is the chemical formula for quicklime?", "Ca(OH)2", "CaCO3", "CaO", "CaCl2", "C", "Quicklime is calcium oxide (CaO).")
    ],
    "Acids, Bases and Salts": [
        ("What is the pH of a neutral solution?", "7", "0", "14", "5", "A", "A neutral solution has a pH of exactly 7 at room temperature.")
    ],
    "Life Processes": [
        ("Which part of the plant is responsible for transpiration?", "Xylem", "Phloem", "Stomata", "Root hairs", "C", "Stomata are tiny pores on leaves that release water vapor."),
        ("Which of the following is the energy currency of the cell?", "DNA", "RNA", "ATP", "ADP", "C", "ATP (Adenosine Triphosphate) stores and provides energy for cellular processes.")
    ],
    "Light - Reflection and Refraction": [
        ("The focal length of a plane mirror is:", "Zero", "Infinite", "25 cm", "-25 cm", "B", "A plane mirror has an infinite radius of curvature, hence its focal length is infinite.")
    ],
    "Electricity": [
        ("The SI unit of electric current is:", "Volt", "Ohm", "Ampere", "Watt", "C", "Ampere is the SI unit of electric current.")
    ],
    "Arithmetic Progressions": [
        ("If the common difference of an AP is 5, then what is a_18 - a_13?", "5", "20", "25", "30", "C", "The difference between the 18th and 13th term is 5d = 5 * 5 = 25.")
    ],
    "Triangles": [
        ("If a line is drawn parallel to one side of a triangle, it divides the other two sides in:", "Different ratios", "The same ratio", "Equal halves", "None", "B", "This is Thales's Theorem or the Basic Proportionality Theorem.")
    ],
    "Real Numbers": [
        ("The decimal expansion of an irrational number is:", "Terminating", "Non-terminating recurring", "Non-terminating non-recurring", "None", "C", "Irrational numbers have non-terminating and non-recurring decimal expansions.")
    ],
    "The First War of Indian Independence (1857)": [
        ("Who was the last Mughal Emperor exiled to Rangoon?", "Akbar II", "Shah Alam II", "Bahadur Shah Zafar", "Jahangir", "C", "Bahadur Shah Zafar was exiled to Rangoon by the British after the 1857 revolt.")
    ],
    "Era of Gandhi and National Movement": [
        ("In which year did the Dandi March take place?", "1920", "1930", "1942", "1919", "B", "The Dandi March (Salt Satyagraha) began on March 12, 1930.")
    ]
}

def get_generic_question(subject, ch, index):
    if subject == "SCIENCE":
        return (f"Which of the following describes a core principle of {ch}?", "Observation A", "Observation B", "Observation C", "Observation D", "A", f"This is an essential scientific concept covered in {ch}.")
    elif subject == "MATH":
        return (f"Calculate the value related to {ch} given x={index}:", f"{10 * index}", f"{20 * index}", f"{30 * index}", f"{40 * index}", "A", f"Applying the formulas from {ch} yields this result.")
    else:
        return (f"Identify the major event in {ch}:", "Event A", "Event B", "Event C", "Event D", "A", f"Historical records show this is central to {ch}.")

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

def add_questions(subject, subject_name, chapter_list):
    global chapter_id_counter
    global out
    out += f"        // {subject_name}\n"
    for i, ch in enumerate(chapter_list):
        out += f'        chapters.add(Chapter(id = {chapter_id_counter}L, subject = "{subject}", chapterName = "{ch}", orderIndex = {i}))\n'
        
        real_qs = mock_questions.get(ch, [])
        for q_index in range(1, 6):
            if q_index - 1 < len(real_qs):
                qt, oa, ob, oc, od, ans, exp = real_qs[q_index - 1]
            else:
                qt, oa, ob, oc, od, ans, exp = get_generic_question(subject, ch, q_index)
            
            # Escape quotes if necessary
            qt = qt.replace('"', '\\"')
            exp = exp.replace('"', '\\"')
            out += f'        questions.add(QuizQuestion(chapterId = {chapter_id_counter}L, questionText = "{qt}", optionA = "{oa}", optionB = "{ob}", optionC = "{oc}", optionD = "{od}", correctOption = "{ans}", explanation = "{exp}"))\n'
        chapter_id_counter += 1

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
