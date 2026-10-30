package com.example.bloom.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdaptiveEngine {

    public static final List<Suggestion> ACTIVITIES = Arrays.asList(

            new Suggestion("a1", "Full Body Energy Boost", "🔥", "Full body + energy", "20 min", 25, "amazing", 3)
                    .withExercise("Squats (12), Glute bridges (15), Knee raises (12), Bird dog (10), Wall push-ups (12), Leg lifts (12), Marching (45s)", "Energy & Strength"),
            new Suggestion("a2", "Strong Legs & Core", "💪", "Legs + glutes + core", "20 min", 25, "amazing", 3)
                    .withExercise("Glute bridges (15), Sit-to-stand (10), Leg lifts (15), Dead bug (10), Bird dog (10), Calf raises (15), Marching (45s)", "Strength & Stability"),
            new Suggestion("a3", "Pilates Full Body Flow", "🧘", "Core + mobility + posture", "25 min", 30, "amazing", 2)
                    .withExercise("Pelvic tilts (10), Glute bridges (15), Dead bug (10), Bird dog (10), Leg lifts (12), Leg circles (8), Cat-cow (8), Child's pose", "Mobility & Flow"),
            new Suggestion("a4", "Standing Cardio Flow", "💃", "Cardio + coordination", "15 min", 25, "amazing", 3)
                    .withExercise("Marching (60s), Step-touch (45s), Knee raises (30s), Side steps (45s), Arm circles (30s), Punches (30s)", "Heart Health"),
            new Suggestion("a5", "Feel-Good Strength", "✨", "Full-body strength", "20 min", 25, "amazing", 3)
                    .withExercise("Chair squats (12), Glute bridges (15), Wall push-ups (12), Bird dog (10), Hip abduction (12), Calf raises (15), Dead bug (10)", "Full Body Power"),

            new Suggestion("a6", "15-Minute Full Body", "🌟", "Daily habit builder", "15 min", 20, "good", 2)
                    .withExercise("March (60s), Chair squats (10), Wall push-ups (10), Glute bridges (12), Bird dog (8), Leg lifts (10)", "Balanced Energy"),
            new Suggestion("a7", "Gentle Strength Flow", "🌿", "Functional strength", "15 min", 20, "good", 2)
                    .withExercise("Glute bridges (12), Sit-to-stand (10), Wall push-ups (10), Dead bug (8), Calf raises (12), Hip extension (10)", "Steady Progress"),
            new Suggestion("a8", "Pilates Core & Posture", "🕯️", "Alignment & breath", "20 min", 20, "good", 2)
                    .withExercise("Pelvic tilts (10), Dead bug (8), Bird dog (8), Glute bridge (12), Cat-cow (8), Chest opening, Child's pose", "Core Focus"),
            new Suggestion("a9", "Low-Impact Cardio", "👟", "Steady state cardio", "15 min", 20, "good", 2)
                    .withExercise("March (60s), Step-touch (60s), Side steps (45s), Knee raises (30s), Punches (30s), Slow march (60s)", "Vitality"),
            new Suggestion("a10", "Lower Body & Core", "🦵", "Foundation builder", "20 min", 20, "good", 2)
                    .withExercise("Chair squats (10), Glute bridges (15), Leg lifts (12), Dead bug (8), Bird dog (8), Calf raises (15)", "Lower Power"),

            new Suggestion("a11", "10-Minute Mood Reset", "☀️", "Gentle spark", "10 min", 15, "meh", 1)
                    .withExercise("Slow march (60s), Shoulder rolls (30s), Side steps (60s), Glute bridges (10), Bird dog (6), Cat-cow (8), Breathing", "Mood Lift"),
            new Suggestion("a12", "Easy Full Body", "🎈", "Effortless movement", "12 min", 15, "meh", 1)
                    .withExercise("March (60s), Wall push-ups (8), Sit-to-stand (8), Glute bridges (10), Leg lifts (8), Bird dog (6)", "Gentle Activation"),
            new Suggestion("a13", "Gentle Pilates", "🌊", "Flow with ease", "15 min", 15, "meh", 1)
                    .withExercise("Pelvic tilts (10), Glute bridges (10), Dead bug (6), Bird dog (6), Cat-cow (8), Child's pose, Breathing", "Relaxed Flow"),
            new Suggestion("a14", "Stretch & Move", "🏹", "Mobility + relaxation", "12 min", 15, "meh", 1)
                    .withExercise("Neck mobility, Shoulder rolls, Cat-cow, Side stretch, Hip circles, Calf stretch, Hamstring stretch", "Flexibility"),
            new Suggestion("a15", "Light Legs & Core", "🕊️", "Simple foundation", "12 min", 15, "meh", 1)
                    .withExercise("Glute bridges (10), Hip abduction (8), Calf raises (10), Bird dog (6), Dead bug (6), Gentle marching", "Soft Strength"),

            new Suggestion("a16", "Gentle Morning Move", "🌤️", "Quiet start", "10 min", 10, "low", 1)
                    .withExercise("Breathing, Shoulder rolls, Gentle march, Cat-cow, Glute bridges, Bird dog, Child's pose", "Awakening"),
            new Suggestion("a17", "10-Minute Mobility", "🌀", "Joint health", "10 min", 10, "low", 1)
                    .withExercise("Neck/Shoulder circles, Cat-cow, Side stretch, Hip/Ankle circles, Calf stretch, Slow breathing", "Range of Motion"),
            new Suggestion("a18", "Gentle Core & Posture", "📐", "Subtle alignment", "10 min", 10, "low", 1)
                    .withExercise("Pelvic tilts (8), Dead bug (5), Bird dog (5), Glute bridges (8), Chest opening, Child's pose", "Back Support"),
            new Suggestion("a19", "Slow Full Body Flow", "🐢", "Mindful pace", "12 min", 10, "low", 1)
                    .withExercise("Slow march, Shoulder rolls, Wall push-ups (6), Sit-to-stand (6), Glute bridges (8), Leg lifts (6), Stretching", "Calm Energy"),
            new Suggestion("a20", "Evening Relaxation", "🌙", "Wind down", "10 min", 10, "low", 1)
                    .withExercise("Breathing, Neck/Shoulder stretch, Cat-cow, Forward stretch, Side stretch, Child's pose", "Restful Move"),

            new Suggestion("a21", "10-Minute Stress Release", "🍃", "Tension melting", "10 min", 15, "stressed", 1)
                    .withExercise("Deep breathing, Shoulder rolls, Slow march, Cat-cow, Bird dog, Side stretch, Child's pose", "Inner Peace"),
            new Suggestion("a22", "Gentle Pilates for Stress", "🎐", "Breath-led flow", "15 min", 15, "stressed", 1)
                    .withExercise("Pelvic tilts (8), Glute bridges (10), Dead bug (6), Bird dog (6), Cat-cow (8), Leg lifts (8), Child's pose", "Somatic Flow"),
            new Suggestion("a23", "Tension Release Flow", "🌊", "Stress shedding", "12 min", 15, "stressed", 1)
                    .withExercise("Neck/Shoulder mobility, Arm opening, Cat-cow, Side stretch, Hip circles, Calf stretch, Deep breathing", "Unwinding"),
            new Suggestion("a24", "Slow Mindful Movement", "🐚", "Mind-body connection", "10 min", 15, "stressed", 1)
                    .withExercise("Slow march (2m), Shoulder rolls, Side steps, Bird dog (6), Glute bridges (8), Cat-cow (6), Deep breathing", "Grounding"),
            new Suggestion("a25", "Bedtime Wind-Down", "🛌", "Sleep prep", "10 min", 15, "stressed", 1)
                    .withExercise("Breathing, Neck/Shoulder stretch, Side stretch, Hamstring stretch, Child's pose, Legs relaxed", "Deep Rest")
    );

    public static final List<Suggestion> RECIPES = Arrays.asList(

            new Suggestion("r1", "Mediterranean Chicken Bowl", "🥗", "Protein: 43g · Carbs: 42g", "25 min", 0, "amazing", 3)
                    .withRecipe("Chicken breast (150g), Quinoa (100g), Cucumber, Tomato, Avocado, Feta, Olive oil, Lemon juice",
                            "1. Grill seasoned chicken. 2. Slice veggies. 3. Build bowl with quinoa, veggies, and chicken. 4. Drizzle oil & lemon."),
            new Suggestion("r2", "Teriyaki Salmon Rice Bowl", "🍣", "Protein: 35g · Carbs: 48g", "25 min", 0, "amazing", 3)
                    .withRecipe("Salmon fillet (150g), Brown rice (120g), Broccoli, Carrot, Soy sauce, Honey, Rice vinegar, Garlic, Sesame seeds",
                            "1. Mix sauce. 2. Pan-cook salmon. 3. Sauté veggies. 4. Serve over rice and top with sesame seeds."),
            new Suggestion("r3", "Creamy Tomato Spinach Pasta", "🍝", "Protein: 20g · Carbs: 62g", "25 min", 0, "amazing", 3)
                    .withRecipe("Whole-wheat pasta (75g), Tomato passata (150g), Spinach, Parmesan, Olive oil, Garlic, Onion, Basil",
                            "1. Cook pasta. 2. Sauté onion/garlic in oil. 3. Add passata and simmer. 4. Wilt spinach in sauce. 5. Toss with pasta."),
            new Suggestion("r4", "Chicken & Avocado Wrap", "🌯", "Protein: 38g · Carbs: 40g", "15 min", 0, "amazing", 2)
                    .withRecipe("Whole-grain tortilla, Chicken (120g), Avocado, Tomato, Lettuce, Greek yogurt, Lemon",
                            "1. Cook chicken. 2. Mash avocado. 3. Spread avocado, add veggies and chicken. 4. Top with yogurt and wrap."),
            new Suggestion("r5", "Berry Yogurt Breakfast Bowl", "🍓", "Protein: 23g · Carbs: 48g", "5 min", 0, "amazing", 1)
                    .withRecipe("Greek yogurt (200g), Berries (100g), Banana, Oats, Chia seeds, Honey",
                            "1. Put yogurt in bowl. 2. Top with fruit, oats, and chia. 3. Drizzle with honey."),

            new Suggestion("r6", "Chicken Caesar Salad", "🥗", "Protein: 42g · Carbs: 22g", "15 min", 0, "good", 2)
                    .withRecipe("Chicken (130g), Romaine lettuce, Parmesan, Whole-grain bread, Greek yogurt, Lemon, Mustard, Garlic",
                            "1. Grill chicken. 2. Toast bread for croutons. 3. Mix yogurt, lemon, mustard for dressing. 4. Combine and toss."),
            new Suggestion("r7", "Veggie & Egg Breakfast Toast", "🥑", "Protein: 20g · Carbs: 28g", "10 min", 0, "good", 1)
                    .withRecipe("Whole-grain bread, Eggs (2), Avocado, Tomato, Spinach, Olive oil",
                            "1. Toast bread. 2. Mash avocado on top. 3. Cook eggs and sauté spinach. 4. Assemble toast."),
            new Suggestion("r8", "Tuna Avocado Salad", "🥗", "Protein: 32g · Carbs: 15g", "10 min", 0, "good", 1)
                    .withRecipe("Tuna (120g), Avocado, Cucumber, Tomato, Lettuce, Olive oil, Lemon",
                            "1. Chop veggies. 2. Add tuna and avocado. 3. Dress with oil and lemon."),
            new Suggestion("r9", "Chicken Vegetable Stir-Fry", "🥘", "Protein: 40g · Carbs: 40g", "20 min", 0, "good", 2)
                    .withRecipe("Chicken (150g), Brown rice (120g), Broccoli, Bell pepper, Carrot, Soy sauce, Sesame oil, Garlic",
                            "1. Sauté chicken in oil. 2. Add garlic and veggies. 3. Stir-fry 5 mins. 4. Add soy sauce and serve over rice."),
            new Suggestion("r10", "Apple Cinnamon Overnight Oats", "🍎", "Protein: 20g · Carbs: 53g", "5 min", 0, "good", 1)
                    .withRecipe("Oats (45g), Greek yogurt (120g), Apple, Chia seeds, Cinnamon, Honey",
                            "1. Mix all in container. 2. Refrigerate overnight. 3. Serve with extra cinnamon."),

            new Suggestion("r11", "Cheesy Egg & Avocado Toast", "🍳", "Protein: 23g · Carbs: 27g", "10 min", 0, "meh", 1)
                    .withRecipe("Whole-grain bread, Eggs (2), Avocado, Light cheese (25g), Tomato",
                            "1. Toast bread. 2. Mash avocado. 3. Cook eggs. 4. Melt cheese on eggs and place on toast."),
            new Suggestion("r12", "Chicken & Rice Comfort Bowl", "🍚", "Protein: 38g · Carbs: 43g", "15 min", 0, "meh", 1)
                    .withRecipe("Chicken (130g), Rice (120g), Cucumber, Carrot, Greek yogurt, Lemon",
                            "1. Warm chicken/rice. 2. Mix yogurt/lemon sauce. 3. Build bowl and drizzle with sauce."),
            new Suggestion("r13", "Creamy Tomato & Lentil Soup", "🥣", "Protein: 19g · Carbs: 47g", "30 min", 0, "meh", 2)
                    .withRecipe("Lentils (150g), Tomato passata (200g), Onion, Garlic, Olive oil, Stock, Yogurt, Basil",
                            "1. Sauté onion/garlic. 2. Add passata, stock, lentils. 3. Simmer 15 mins. 4. Blend partially and serve with yogurt."),
            new Suggestion("r14", "Greek Yogurt Fruit Bowl", "🥣", "Protein: 23g · Carbs: 38g", "5 min", 0, "meh", 1)
                    .withRecipe("Greek yogurt (200g), Berries, Banana, Walnuts, Chia seeds, Cinnamon",
                            "1. Yogurt in bowl. 2. Add fruit and nuts. 3. Sprinkle cinnamon."),
            new Suggestion("r15", "Easy Chicken Quesadilla", "🌮", "Protein: 34g · Carbs: 38g", "15 min", 0, "meh", 2)
                    .withRecipe("Tortilla, Chicken (100g), Cheese (40g), Bell pepper, Salsa, Olive oil",
                            "1. Fill tortilla with chicken, peppers, cheese. 2. Fold and pan-cook 3 mins per side. 3. Serve with salsa."),

            new Suggestion("r16", "Chicken & Vegetable Soup", "🥣", "Protein: 35g · Carbs: 25g", "30 min", 0, "low", 1)
                    .withRecipe("Chicken (120g), Carrot, Zucchini, Celery, Onion, Stock (400ml), Olive oil, Parsley",
                            "1. Sauté onion. 2. Add veggies and stock. 3. Add chicken and simmer 20 mins. 4. Shred chicken and return to soup."),
            new Suggestion("r17", "Creamy Scrambled Eggs & Avocado Toast", "🍳", "Protein: 22g · Carbs: 25g", "10 min", 0, "low", 1)
                    .withRecipe("Eggs (2), Greek yogurt (20g), Bread, Avocado, Tomato",
                            "1. Beat eggs with yogurt and cook slowly. 2. Toast bread and add mashed avocado. 3. Top with eggs."),
            new Suggestion("r18", "Warm Chicken & Sweet Potato Bowl", "🍠", "Protein: 38g · Carbs: 42g", "30 min", 0, "low", 2)
                    .withRecipe("Chicken (130g), Sweet potato (150g), Spinach, Greek yogurt, Paprika, Garlic",
                            "1. Roast sweet potato cubes 20 mins. 2. Cook chicken. 3. Wilt spinach. 4. Combine and serve with yogurt."),
            new Suggestion("r19", "Banana Peanut Butter Yogurt Bowl", "🍌", "Protein: 23g · Carbs: 43g", "5 min", 0, "low", 1)
                    .withRecipe("Greek yogurt (200g), Banana, Peanut butter (15g), Chia seeds, Cinnamon",
                            "1. Yogurt in bowl. 2. Top with sliced banana and peanut butter. 3. Sprinkle cinnamon."),
            new Suggestion("r20", "Tomato & Mozzarella Toast", "🍅", "Protein: 20g · Carbs: 32g", "10 min", 0, "low", 1)
                    .withRecipe("Bread, Mozzarella (60g), Tomato, Basil, Olive oil",
                            "1. Toast bread. 2. Layer tomato and mozzarella. 3. Add basil and oil."),

            new Suggestion("r21", "10-Minute Chicken Avocado Bowl", "🥗", "Protein: 38g · Carbs: 30g", "10 min", 0, "stressed", 1)
                    .withRecipe("Chicken (130g), Rice (80g), Avocado, Cucumber, Tomato, Lemon, Olive oil",
                            "1. Bowl with warm rice/chicken. 2. Add veggies and avocado. 3. Dress with lemon and oil."),
            new Suggestion("r22", "Calming Banana Cinnamon Oatmeal", "🥣", "Protein: 15g · Carbs: 58g", "8 min", 0, "stressed", 1)
                    .withRecipe("Oats (45g), Banana, Greek yogurt (80g), Chia seeds, Cinnamon",
                            "1. Cook oats in water 5 mins. 2. Add half banana while cooking. 3. Top with yogurt, remaining banana, and cinnamon."),
            new Suggestion("r23", "Egg & Vegetable Scramble", "🍳", "Protein: 22g · Carbs: 12g", "10 min", 0, "stressed", 1)
                    .withRecipe("Eggs (2), Spinach, Tomato, Bell pepper, Avocado, Olive oil",
                            "1. Sauté veggies in oil. 2. Pour in beaten eggs. 3. Scramble and serve with avocado."),
            new Suggestion("r24", "Mediterranean Chickpea Bowl", "🥗", "Protein: 15g · Carbs: 44g", "10 min", 0, "stressed", 1)
                    .withRecipe("Chickpeas (150g), Cucumber, Tomato, Feta (30g), Avocado, Olive oil, Lemon, Oregano",
                            "1. Combine chickpeas and veggies. 2. Add avocado and feta. 3. Dress and mix."),
            new Suggestion("r25", "Quick Yogurt Berry Bowl", "🍓", "Protein: 25g · Carbs: 38g", "5 min", 0, "stressed", 1)
                    .withRecipe("Greek yogurt (200g), Berries, Banana, Chia seeds, Oats, Honey",
                            "1. Yogurt in bowl. 2. Top with fruit, oats, and chia. 3. Drizzle honey.")
    );


    public static List<Suggestion> activitiesFor(String mood) {
        String filter = mood == null ? "good" : mood;
        return activitiesForIntensity(filter);
    }

    public static List<Suggestion> activitiesForIntensity(String intensity) {
        List<Suggestion> out = new ArrayList<>();
        for (Suggestion s : ACTIVITIES) if (intensity.equalsIgnoreCase(s.intensity)) out.add(s);
        out.sort((a, b) -> Integer.compare(a.difficulty, b.difficulty));
        return out;
    }

    public static List<Suggestion> recipesFor(String mood) {
        String filter = java.util.Objects.requireNonNullElse(mood, "good");
        return recipesForIntensity(filter);
    }

    public static List<Suggestion> recipesForIntensity(String intensity) {
        List<Suggestion> out = new ArrayList<>();
        for (Suggestion s : RECIPES) if (intensity.equalsIgnoreCase(s.intensity)) out.add(s);
        

        out.sort(java.util.Comparator.comparingInt(a -> a.difficulty));
        return out;
    }


    public static String biboMessage(String mood, String name) {
        if (mood == null) return "Hi " + name + "! I'm Bibo 🐻 How are you feeling today?";
        switch (mood) {
            case "amazing":  return "Yes! I feel your energy ✨ Let's channel it into something epic.";
            case "good":     return "Love that. A little movement keeps this momentum going 🌟";
            case "meh":      return "In-between days count too. Let's pick one small win 💛";
            case "low":      return "I'm right here with you. Tiny steps only today 🤍";
            case "stressed": return "Breathe with me first. Just one gentle thing 🌿";
            default:         return "Hi " + name + "! Let's check in 💛";
        }
    }


    public static String biboReply(String input, String mood, String name) {
        String t = input == null ? "" : input.toLowerCase();
        boolean lowEnergy = "low".equals(mood) || "stressed".equals(mood);

        if (t.contains("pep") || t.contains("motiv"))
            return lowEnergy
                    ? "Hey " + name + ", showing up here IS the win. One breath at a time 🤍"
                    : "You've got this, " + name + "! Pick ONE quest and crush it 💖";
        if (t.contains("easy") || t.contains("small"))
            return "Try 5-minute breathing 🌬️ or a full glass of water 💧 — both earn XP.";
        if (t.contains("stuck"))
            return "Stuck is allowed. Stand up, stretch wide for 30 seconds, come back. I'll wait 🌿";
        if (t.contains("celebrate") || t.contains("proud"))
            return "OKAY YES 🎊 You're building something real. So proud of you!";
        if (t.contains("tired") || t.contains("sad"))
            return "Thanks for telling me. Rest is a healthy habit too — let's do one gentle thing 🤍";
        return biboMessage(mood, name);
    }
}
