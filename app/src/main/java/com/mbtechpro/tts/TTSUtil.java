package com.mbtechpro.tts;

import android.content.Context;
import android.content.Intent;

import com.mbtechpro.tts.discreteSeekbar.TTSPreference;


/**
 * Created by ashwanisingh on 13/10/17.
 */

public class TTSUtil {

    public static final String INTENT_FILTER = "com.REQUEST_PROCESSED";
    public static final String COMMAND = "command";
    public static final int UPDATE_PROGRESS = 1;
    public static final int INITIALIZING = 2;
    public static final int INITIALISED = 3;
    public static final int IS_PLAYING = 4;
    public static final int INIT_FAILED = 5;
    public static final int LANG_NOT_SUPPORTED = 6;
    public static final int STOPPED = 8;
    public static final int COMPLETED = 9;
    public static final int PAUSED = 10;
    public static final int PLAY = 11;

    public static final String SPEECH_TEXT = "speechText";

    public static final String ACTION_KEY = "action";

    public static final String ACTION_INIT = "openLaunchActivity";
    public static final String ACTION_PLAY = "play";
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_STOP = "stop";
    public static final String ACTION_NEXT = "next";
    public static final String ACTION_PREVIOUS = "previous";
    public static final String ACTION_CHECK_PLAYING = "checkPlaying";
    public static final String ACTION_PITCH = "pitch";
    public static final String ACTION_SPEED = "speed";
    public static final String ACTION_LOCALE = "locale";
    public static final String ACTION_FORCE_CLOSE_SERVICE = "forceCloseService";

    public static final String ACTION_MOVE_FORWARD_FEW_SECOND = "moveForwardFewSecond";
    public static final String ACTION_MOVE_BACKWARD_FEW_SECOND = "moveBackwardFewSecond";

    public static void init(Context context, String speechText, String articleId) {
        Intent intent = new Intent(context, SpeechService.class);
        intent.putExtra(TTSUtil.ACTION_KEY, TTSUtil.ACTION_INIT);
        intent.putExtra(TTSUtil.SPEECH_TEXT, speechText);
        intent.putExtra("articleId", articleId);
        context.startService(intent);
    }

    public static void play(Context context) {
        Intent intent = new Intent(context, SpeechService.class);
        intent.putExtra(TTSUtil.ACTION_KEY, TTSUtil.ACTION_PLAY);
        context.startService(intent);
    }

    public static void pause(Context context) {
        Intent intent = new Intent(context, SpeechService.class);
        intent.putExtra(TTSUtil.ACTION_KEY, TTSUtil.ACTION_PAUSE);
        context.startService(intent);
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, SpeechService.class);
        intent.putExtra(TTSUtil.ACTION_KEY, TTSUtil.ACTION_STOP);
        context.startService(intent);
    }

    public static void next(Context context) {
        Intent intent = new Intent(context, SpeechService.class);
        intent.putExtra(TTSUtil.ACTION_KEY, TTSUtil.ACTION_NEXT);
        context.startService(intent);
    }

    public static void previous(Context context) {
        Intent intent = new Intent(context, SpeechService.class);
        intent.putExtra(TTSUtil.ACTION_KEY, TTSUtil.ACTION_PREVIOUS);
        context.startService(intent);
    }

    public static void moveForwardFewSecond(Context context) {
        Intent intent = new Intent(context, SpeechService.class);
        intent.putExtra(TTSUtil.ACTION_KEY, TTSUtil.ACTION_MOVE_FORWARD_FEW_SECOND);
        context.startService(intent);
    }

    public static void moveBackwardFewSecond(Context context) {
        Intent intent = new Intent(context, SpeechService.class);
        intent.putExtra(TTSUtil.ACTION_KEY, TTSUtil.ACTION_MOVE_BACKWARD_FEW_SECOND);
        context.startService(intent);
    }

    public static void sendCheckPlayingRequest(Context context, String articleId) {
        Intent intent = new Intent(context, SpeechService.class);
        intent.putExtra(TTSUtil.ACTION_KEY, TTSUtil.ACTION_CHECK_PLAYING);
        intent.putExtra("articleId", articleId);
        context.startService(intent);
    }

    public static void sendCheckPlayingRequest(Context context, String text, String articleId) {
        Intent intent = new Intent(context, SpeechService.class);
        intent.putExtra(TTSUtil.ACTION_KEY, TTSUtil.ACTION_CHECK_PLAYING);
        intent.putExtra("key", text);
        intent.putExtra("articleId", articleId);
        context.startService(intent);
    }

    public static void openTTSSettingScreen(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.android.settings.TTS_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openTTSAppSettingScreen(Context context) {
        Intent intent = new Intent(context, TTSSettingActivity.class);
        context.startActivity(intent);
    }

    public static void openTTSAppLanguageSettingScreen(Context context) {
        Intent intent = new Intent(context, TTSLanguageSettingActivity.class);
        context.startActivity(intent);
    }

    public static void sendTTSPitch(Context context) {
        if (TTSPreference.getInstance(context).isTTSServiceRunning()) {
            Intent intent = new Intent(context, SpeechService.class);
            intent.putExtra(TTSUtil.ACTION_KEY, TTSUtil.ACTION_PITCH);
            context.startService(intent);
        }
    }

    public static void sendTTSSpeed(Context context) {
        if (TTSPreference.getInstance(context).isTTSServiceRunning()) {
            Intent intent = new Intent(context, SpeechService.class);
            intent.putExtra(TTSUtil.ACTION_KEY, TTSUtil.ACTION_SPEED);
            context.startService(intent);
        }
    }

    public static void sendTTSLocale(Context context) {
        if (TTSPreference.getInstance(context).isTTSServiceRunning()) {
            Intent intent = new Intent(context, SpeechService.class);
            intent.putExtra(TTSUtil.ACTION_KEY, TTSUtil.ACTION_LOCALE);
            context.startService(intent);
        }
    }

    public static void sendTTSForceCloseService(Context context) {
        if (TTSPreference.getInstance(context).isTTSServiceRunning()) {
            Intent intent = new Intent(context, SpeechService.class);
            intent.putExtra(TTSUtil.ACTION_KEY, TTSUtil.ACTION_FORCE_CLOSE_SERVICE);
            context.startService(intent);
        }
    }


    public static final String testString1 = "WASHINGTON — Steve Bannon and his allies " +
            "don't have to win every battle in their guerrilla war to take down the Republican '" +
            "political establishment. They just have to wreak enough havoc to alter the behavior of GOP lawmakers.\n" +
            "\n" +
            "And that's what Bannon, the former chief White House strategist " +
            "and campaign manager for Donald Trump, is promising to do in the corridors of " +
            "Washington power and on the electoral map.\n" +
            "\n" +
            "His core argument: Senate Majority Leader Mitch McConnell, R-Ky., " +
            "and establishment-minded Republican members of Congress are thwarting President Donald Trump's " +
            "agenda through a combination of sabotage and incompetence. Conservative voters should both demand " +
            "a change in Senate GOP leadership and use primary elections to oust Republicans who aren't in lock " +
            "step with Trump, the anti-establishment Republicans " +
            "contend." +
            "Bannon's aggressive strategy and tactics — including a " +
            "full assault on McConnell and securing the financial backing of " +
            "mega-donors— have given new urgency to the insurgency.\n" +
            "\n" +
            "\"The battle royale between Trump and the base vs. McConnell and the establishment " +
            "is upon us,\" said David Bozell, president of For America, one of several conservative " +
            "activist groups that share Bannon's goals." +
            "\"It's just going to get hotter and hotter and hotter,\" added Bozell, who last week called on " +
            "McConnell and his team to resign " +
            "from Senate leadership. \"The litmus test is — whether you're an incumbent or a challenger — " +
            "we’re looking to see who's going to support Mitch McConnell or not. If you're going to support Mitch McConnell " +
            "and this leadership team, there's not going to be an appetite for you.\"\n" +
            "\n" +
            "The fight between the wings of the Republican Party has been raging since " +
            "long before Bannon became a familiar figure on the national political scene last year. " +
            "It coincided with the rise of the Tea Party in the 2010 election cycle and intensified with " +
            "Trump's defeat of a legion of GOP current and former officeholders in the 2016 presidential primary.";

    public static final String testString2 = "A group of words put together to form a group that is " +
            "usually longer than a sentence. " +
            "Paragraphs are often made up of many sentences. " +
            "They are usually between four to eight sentences. " +
            "Paragraphs can begin with an indentation (about five spaces), or " +
            "by missing a line out, and then starting again.";


    public static final String textString3 = "" +
            "Swishhhh In the middle of the night, " +
            "Ismat Ara heard a faint sound. Within seconds, the 27-year-old Ara knew what it meant: her mud and bamboo thatched hut was on fire. " +
            "She sprang from the floor, lifted her three-and-a-half-year-old son Absar and ran out, all in one movement. When she turned to look back, " +
            "her dwelling was engulfed in flames. And then came the shocking realisation: her 13-year-old daughter was still inside the hut. " +
            "Jannat, my daughter, who was sleeping, was consumed by the fire, said Ara in Rohingya. Two weeks previously, " +
            "her husband had been picked up by the Myanmar military. Since then, in late September, Ara has not seen or heard from him. " +
            "So, when other families from her village Sain Dee Pran (Sein Nyin Pya in Burmese) at Buthidaung in northwest Myanmar, " +
            "decided to leave for Bangladesh, Ara and her son had no option but to follow them. " +
            "Ara and her neighbours landed in Shahpori Island, the last point of Bangladesh at the confluence of the river Naf and the Bay of Bengal, " +
            "on the morning of October 12, after an overnight journey by boat. As soon as she, her son, " +
            "and 12 other members of Sain Dee Pran stepped out of a small boat in wet clothes, this reporter spoke to them. " +
            "From the village, we escaped to the mountains [behind the village] and kept walking towards the [Naf] river. " +
            "On the 12th day, we reached the beach, Ara said. On the other side of the beach was Shahpori Island in Bangladesh, " +
            "but the family had no resources to hire a boat to cross the mighty river. " +
            "UNICEF describes hell on earth for Rohingya children Waiting on the beach We waited on the beach, in the open, for about a week before we got a boat, said Rehana Begum, 40, Ara s neighbour. All they had to survive on were biscuits and dried flat rice, which was soon exhausted. To make the food last, the elders stopped eating. The children, said Rehana, were given one packet of biscuit and one half-litre bottle of water for the entire day. A man in a red and white jacket distributed the water, recalled Rehana. The water was not enough, and people were so thirsty that they drank from the confluence, and two persons died after drinking the saline water. They were buried in the marshes. On September 28, when her husband was taken away along with 14 others by the Myanmar military, Rehana was in an advanced stage of pregnancy. Two days later, her house began to burn. She too heard a whooshing sound, which senior officers of the Bangladesh Army say is typical of projectiles from rocket launchers. Once her house burned down, Rehana s family scattered. Two of her daughters, Mohaseffa and Setara, in their late teens, went with their uncle and three others, Taslima, Jasmine and Fatema, simply disappeared. Rehana managed to deliver another daughter in the camp on Naik-Kon Dia beach in Maungdaw, where she and the rest were sheltering. But the baby died in three days. I did not bother to name the child, said Rehana who arrived in Shahpori with her father and one daughter, Sabera. Even Sabera is afflicted with some strange problem. She cannot hold her head properly, said 72-year-old Salem, Rehana s father. Ara s and Rehana s stories are not unique. Over 50 families interviewed by this reporter over 10 days indicate that each one of the 536,000 refugees, who reached south-east Bangladesh between August 25 and October 11, predominantly from three places of north-west Myanmar Buthidaung, Maungdaw and Rathidaung have similar stories to narrate. Another 10,000 refugees have since arrived and the enormity of the crisis has shaken everyone, especially doctors. There are cases of children s head being hit with heavy objects, and men and women without fingers and with bullets in critical parts of the body, around the nape of the neck, or spine, are arriving in thousands, said Aaron Jackson, a doctor from a non-profit organisation, Planting Peace. Dr. Jackson had been working in the area for the past month. He showed footages, shot by his team of professional camera persons, of babies with severe head injuries. This is genocide, just that the U.S. or the UN is not saying so as they will have to take responsibility, said Dr. Jackson, a resident of Florida. Nearly 58% of Rohingya refugees in Bangladesh are children  Yanghee Lee, the UN Special Rapporteur of Human Rights in Myanmar, told The Hindu in the first week of September that more than a thousand Rohingya have died, while the head of UN Migration Agency William Lacy Swing expressed concern over faster refugee influx than anywhere in the world. Analysing satellite images, Human Rights Watch (HRW) has indicated that 66 more Rohingya villages have been torched, refuting claims of Myanmar leader Aung San Suu Kyi that military operation has ended since September 5. " +
            "As many as 288 villages in Rakhine State have been partially or fully destroyed since August 25, claimed HRW. " +
            "With the attacks, the influx continues. According to the Bangladesh government s data, " +
            "Rohingya are entering the country through nine points in a 217-km land border, " +
            "besides a 54-km riverine border that has multiple entry points. " +
            "The major entry points may have multiple sub-points of entry, said a senior official. " +
            "Bangladesh has kept the land border open while restricting the movement in the Naf river. Ara and Rehana, however, " +
            "entered through a riverine border in Shapori Island s Gholapara point and were taken to a friendly internment camp, " +
            "made of plastic and sliced bamboo, for preliminary investigation. " +
            "In one such all-sides-open camp with plastic sheets for roofing, in a rubber plantation in Kutupalang area in Cox s Bazar, " +
            "about one hundred refugees were asked by this correspondent on Wednesday if they had lost anyone in their family. " +
            "About 40% raised their hand. Each one of the 139 refugees raised their hand, asked whether their house had been set on fire. " +
            "Rohingya not entitled to refugee status, says brief concept note on ICHR seminar  New families formed Jannat Ali, " +
            "a fisherman in Myanmar s Maungdaw district, said he lost his wife and three children. But here [in Balukhali camp in Cox s Bazar], " +
            "I met two children who lost their parents; we are now raising the tent together. " +
            "The children are cooking and I am carrying the food stuff and slicing the bamboos, he said. " +
            "Sitara Begum, herself a Rohingya who is settled in a 1991-92 camp in Nayapara in Teknaf sub-district, has adopted Samira Begum, " +
            "17, who lost both her parents. She was in the boat with my husband s nephew and had no place to go. " +
            "Initially, we provided her shelter for a night but then realised that it would be a better idea to let her stay with us, she said. " +
            "Her neighbour Yasmin adopted a seven-year-old child, Taslima, who claimed to have witnessed her father being shot and killed. " +
            "If this continues, the Rohingya community, whatever little of them is still left in Myanmar, will be extinct, " +
            "said Faisal Alam, a human rights activist.";

    public static final String textString4 = "" +
            "Even Sabera is afflicted with some strange problem. She cannot hold her head properly, said 72-year-old Salem, Rehana s father. " +
            "Ara s and Rehana s stories are not unique. " +
            "Over 50 families interviewed by this reporter over 10 days indicate that each one of the 536,000 refugees, " +
            "who reached south-east Bangladesh between August 25 and October 11, predominantly from three places of " +
            "north-west Myanmar Buthidaung, Maungdaw and Rathidaung have similar stories to narrate. Another 10,000 refugees have since arrived " +
            "and the enormity of the crisis has shaken everyone, especially doctors. There are cases of children s head being hit with heavy objects, " +
            "and men and women without fingers and with bullets in critical parts of the body, around the nape of the neck, or spine, are arriving in " +
            "thousands, said Aaron Jackson, a doctor from a non-profit organisation, Planting Peace. Dr. Jackson had been working in the area for the " +
            "past month. He showed footages, shot by his team of professional camera persons, of babies with severe head injuries. " +
            "This is genocide, just that the U.S. or the UN is not saying so as they will have to take responsibility, said Dr. " +
            "Jackson, a resident of Florida. Nearly 58% of Rohingya refugees in Bangladesh are children  Yanghee Lee, the UN Special Rapporteur of " +
            "Human Rights in Myanmar, told The Hindu in the first week of September that more than a thousand Rohingya have died, while the head of UN " +
            "Migration Agency William Lacy Swing expressed concern over faster refugee influx than anywhere in the world. Analysing satellite images, " +
            "Human Rights Watch (HRW) has indicated that 66 more Rohingya villages have been torched, refuting claims of Myanmar leader Aung San Suu " +
            "Kyi that military operation has ended since September 5. " +
            "As many as 288 villages in Rakhine State have been partially or fully destroyed since August 25, claimed HRW. " +
            "With the attacks, the influx continues. According to the Bangladesh government s data, " +
            "Rohingya are entering the country through nine points in a 217-km land border, " +
            "besides a 54-km riverine border that has multiple entry points. " +
            "The major entry points may have multiple sub-points of entry, said a senior official. " +
            "Bangladesh has kept the land border open while restricting the movement in the Naf river. Ara and Rehana, however, " +
            "entered through a riverine border in Shapori Island s Gholapara point and were taken to a friendly internment camp, " +
            "made of plastic and sliced bamboo, for preliminary investigation. " +
            "In one such all-sides-open camp with plastic sheets for roofing, in a rubber plantation in Kutupalang area in Cox s Bazar, " +
            "about one hundred refugees were asked by this correspondent on Wednesday if they had lost anyone in their family. " +
            "About 40% raised their hand. Each one of the 139 refugees raised their hand, asked whether their house had been set on fire. " +
            "Rohingya not entitled to refugee status, says brief concept note on ICHR seminar  New families formed Jannat Ali, " +
            "a fisherman in Myanmar s Maungdaw district, said he lost his wife and three children. But here [in Balukhali camp in Cox s Bazar], " +
            "I met two children who lost their parents; we are now raising the tent together. " +
            "The children are cooking and I am carrying the food stuff and slicing the bamboos, he said. " +
            "Sitara Begum, herself a Rohingya who is settled in a 1991-92 camp in Nayapara in Teknaf sub-district, has adopted Samira Begum, " +
            "17, who lost both her parents. She was in the boat with my husband s nephew and had no place to go. " +
            "Initially, we provided her shelter for a night but then realised that it would be a better idea to let her stay with us, she said. " +
            "Her neighbour Yasmin adopted a seven-year-old child, Taslima, who claimed to have witnessed her father being shot and killed. " +
            "If this continues, the Rohingya community, whatever little of them is still left in Myanmar, will be extinct, " +
            "said Faisal Alam, a human rights activist.";



}
