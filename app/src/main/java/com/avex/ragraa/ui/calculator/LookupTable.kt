package com.avex.ragraa.ui.calculator

//This was a pain to write.

fun mcaLookup(mca: Int, obtained: Int): String {
    val grade = when (mca) {
        in 30..32 -> {
            when (obtained) {
                in 30..<mca + 3 -> "C+"
                in mca + 3..<mca + 8 -> "B-"
                in mca + 8..<mca + 13 -> "B"
                in mca + 13..<mca + 18 -> "A-"
                in mca + 18..<mca + 23 -> "A"
                in mca + 23..100 -> "A+"
                else -> "F"
            }
        }

        in 33..37 -> {
            when (obtained) {
                in 30..<mca - 2 -> "C"
                in mca - 2..<mca + 3 -> "C+"
                in mca + 3..<mca + 8 -> "B-"
                in mca + 8..<mca + 13 -> "B"
                in mca + 13..<mca + 18 -> "B+"
                in mca + 18..<mca + 23 -> "A-"
                in mca + 23..<mca + 28 -> "A"
                in mca + 28..100 -> "A+"
                else -> "F"
            }
        }

        in 38..42 -> {
            when (obtained) {
                in 30..<mca - 7 -> "C-"
                in mca - 7..<mca - 2 -> "C"
                in mca - 2..<mca + 3 -> "C+"
                in mca + 3..<mca + 8 -> "B-"
                in mca + 8..<mca + 13 -> "B"
                in mca + 13..<mca + 18 -> "B+"
                in mca + 18..<mca + 23 -> "A-"
                in mca + 23..<mca + 28 -> "A"
                in mca + 28..100 -> "A+"
                else -> "F"
            }
        }

        in 43..47 -> {
            when (obtained) {
                in 30..<mca - 13 -> "D+"
                in mca - 13..<mca - 7 -> "C-"
                in mca - 7..<mca - 2 -> "C"
                in mca - 2..<mca + 3 -> "C+"
                in mca + 3..<mca + 8 -> "B-"
                in mca + 8..<mca + 13 -> "B"
                in mca + 13..<mca + 18 -> "B+"
                in mca + 18..<mca + 23 -> "A-"
                in mca + 23..<mca + 28 -> "A"
                in mca + 28..100 -> "A+"
                else -> "F"
            }
        }

        in 48..49 -> {
            when (obtained) {
                in 30..mca - 18 -> "D"
                in mca - 18..<mca - 13 -> "D+"
                in mca - 13..<mca - 7 -> "C-"
                in mca - 7..<mca - 2 -> "C"
                in mca - 2..<mca + 3 -> "C+"
                in mca + 3..<mca + 8 -> "B-"
                in mca + 8..<mca + 13 -> "B"
                in mca + 13..<mca + 18 -> "B+"
                in mca + 18..<mca + 23 -> "A-"
                in mca + 23..<mca + 28 -> "A"
                in mca + 28..100 -> "A+"
                else -> "F"
            }
        }

        in 50..52 -> {
            when (obtained) {
                in 30..<mca - 17 -> "D+"
                in mca - 17..<mca - 12 -> "C-"
                in mca - 12..<mca - 7 -> "C"
                in mca - 7..<mca - 2 -> "C+"
                in mca - 2..<mca + 3 -> "B-"
                in mca + 3..<mca + 8 -> "B"
                in mca + 8..<mca + 13 -> "B+"
                in mca + 13..<mca + 18 -> "A-"
                in mca + 18..<mca + 23 -> "A"
                in mca + 23..100 -> "A+"
                else -> "F"
            }
        }

        in 53..57 -> {
            when (obtained) {
                in 30..<mca - 22 -> "D"
                in mca - 22..<mca - 17 -> "D+"
                in mca - 17..<mca - 12 -> "C-"
                in mca - 12..<mca - 7 -> "C"
                in mca - 7..<mca - 2 -> "C+"
                in mca - 2..<mca + 3 -> "B-"
                in mca + 3..<mca + 8 -> "B"
                in mca + 8..<mca + 13 -> "B+"
                in mca + 13..<mca + 18 -> "A-"
                in mca + 18..<mca + 23 -> "A"
                in mca + 23..100 -> "A+"
                else -> "F"
            }
        }

        in 58..64 -> {
            when (obtained) {
                in mca - 27..<mca - 22 -> "D"
                in mca - 22..<mca - 17 -> "D+"
                in mca - 17..<mca - 12 -> "C-"
                in mca - 12..<mca - 7 -> "C"
                in mca - 7..<mca - 2 -> "C+"
                in mca - 2..<mca + 3 -> "B-"
                in mca + 3..<mca + 8 -> "B"
                in mca + 8..<mca + 13 -> "B+"
                in mca + 13..<mca + 18 -> "A-"
                in mca + 18..<mca + 23 -> "A"
                in mca + 23..100 -> "A+"
                else -> "F"
            }
        }

        in 65..76 -> {
            when (obtained) {
                in mca - 32..<mca - 27 -> "D"
                in mca - 27..<mca - 22 -> "D+"
                in mca - 22..<mca - 17 -> "C-"
                in mca - 17..<mca - 12 -> "C"
                in mca - 12..<mca - 7 -> "C+"
                in mca - 7..<mca - 2 -> "B-"
                in mca - 2..<mca + 3 -> "B"
                in mca + 3..<mca + 8 -> "B+"
                in mca + 8..<mca + 13 -> "A-"
                in mca + 13..<mca + 18 -> "A"
                in mca + 18..100 -> "A+"
                else -> "F"
            }
        }

        in 77..91 -> {
            when (obtained) {
                in 95..100 -> "A+"
                in 90..<95 -> "A"
                in mca - 32..<mca - 27 -> "D"
                in mca - 27..<mca - 22 -> "D+"
                in mca - 22..<mca - 17 -> "C-"
                in mca - 17..<mca - 12 -> "C"
                in mca - 12..<mca - 7 -> "C+"
                in mca - 7..<mca - 2 -> "B-"
                in mca - 2..<mca + 3 -> "B"
                in mca + 3..<mca + 8 -> "B+"
                in mca + 8..<mca + 13 -> "A-"
                else -> "F"
            }
        }

        else -> when (obtained) {
            in 90..100 -> "A+"
            in 86..<90 -> "A"
            in 82..<86 -> "A-"
            in 78..<82 -> "B+"
            in 74..<78 -> "B"
            in 70..<74 -> "B-"
            in 66..<79 -> "C+"
            in 62..<66 -> "C"
            in 58..<62 -> "C-"
            in 54..<58 -> "D+"
            in 50..<54 -> "D"
            else -> "F"
        }
    }

    return grade
}