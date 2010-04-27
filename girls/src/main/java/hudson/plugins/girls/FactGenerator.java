/**
 * Copyright (c) 2009 Cliffano Subagio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.girls;

import java.util.Random;

/**
 * {@link FactGenerator} provides Girls facts.
 * @author cliffano
 */
public class FactGenerator {

    private static final String[] FACTS = {
        "\"Big girls need big diamonds.\" Elizabeth Taylor",
        "\"Put your hand on a hot stove for a minute, and it seems like an hour. Sit with a pretty girl for an hour, and it seems like a minute. That's relativity.\" Albert Einstein",
        "\"A girl should be two things: classy and fabulous.\" Coco Chanel",
        "\"I was about half in love with her by the time we sat down. That's the thing about girls. Every time they do something pretty... you fall half in love with them, and then you never know where the hell you are.\" J.D. Salinger",
        "\"It's the good girls who keep diaries; the bad girls never have the time.\" Tallulah Bankhead",
        "\"Every girl should use what Mother Nature gave her before Father Time takes it away.\" Dr. Laurence J. Peter",
        "\"Any girl can look glamorous. All you have to do is stand still and look stupid.\" Hedy Lamarr",
        "\"Girls are like pianos. When they're not upright, they're grand.\" Benny Hill",
        "\"Of the delights of this world, man cares most for sexual intercouse, yet he has left it out of his heaven.\" Mark Twain",
        "\"As to marriage or celibacy, let a man take the course he will. He will be sure to repent.\" Socrates",
        "\"A husband is what's left of the lover after the nerve has been extracted.\" Helen Rowland",
        "\"A bachelor is a selfish, undeserving guy who has cheated some woman out of a divorce.\" Don Quinn",
        "\"Marriage is a great institution, but I'm not ready for an institution yet.\" Mae West",
        "\"My advice to you is to get married. If you find a good wife, you'll be happy; if not, you'll become a philosopher.\" Socrates",
        "\"Never invest your money in anything that eats or needs painting.\" Billy Rose",
        "\"A man can't be too careful in the choice of his enemies.\" Oscar Wilde",
        "\"There are three faithful friends—an old wife, an old dog, and ready money.\" Benjamin Franklin",
        "\"Always forgive your enemies; nothing annoys them so much.\" Oscar Wilde",
        "\"I can resist everything except temptation.\" Oscar Wilde",
        "\"To cease smoking is the easiest thing. I ought to know. I've done it a thousand times.\" Mark Twain",
        "\"A conclusion is simply the place where someone got tired of thinking.\" Arthur Block",
        "\"Common sense is the collection of prejudices acquired by age eighteen.\" Albert Einstein",
        "\"The trouble with being punctual is that nobody's there to appreciate it.\" Franklin P. Jones",
        "\"All my life, I always wanted to be somebody. Now I see that I should have been more specific.\" Jane Wagner",
        "\"Bigamy is having one wife too many. Monogamy is the same.\" Oscar Wilde",
        "\"I am not a vegetarian because I love animals; I am a vegetarian because I hate plants.\" A. Whitney Brown",
        "\"Experience is that marvellous thing that enables you recognise a mistake when you make it again.\" F. P. Jones",
        "\"Advice is what we ask for when we already know the answer but wish we didn't.\" Erica Jong",
        "\"The direct use of force is such a poor solution to any problem, it is generally employed only by small children and large nations.\" David Friedman",
        "\"Man invented language to satisfy his deep need to complain.\" Lily Tomlin",
        "\"Start every day off with a smile and get it over with.\" W. C. Fields",
        "\"The most exciting phrase to hear in science, the one that heralds new discoveries, is not 'Eureka!' (I found it!) but 'That's funny ...'\" Isaac Asimov",
        "\"Treat your password like your toothbrush. Don't let anybody else use it, and get a new one every six months.\" Clifford Stoll",
        "\"Computers are like Old Testament gods; lots of rules and no mercy.\" Joseph Campbell",
        "\"Hardware: the parts of a computer that can be kicked. Software: the parts of a computer that can be shouted\" Jeff Pesis",
        "\"Brigands demand your money or your life; women require both.\" Samuel Butler",
        "\"Women are like cars: we all want a Ferrari, sometimes want a pickup truck, and end up with a station wagon.\" Tim Allen"        
        };

    /**
     * Random instance.
     */
    private static final Random RANDOM = new Random();;

    /**
     * Retrieves a random fact.
     * @return a random fact
     */
    public String random() {
        return FACTS[RANDOM.nextInt(FACTS.length)];
    }
}
