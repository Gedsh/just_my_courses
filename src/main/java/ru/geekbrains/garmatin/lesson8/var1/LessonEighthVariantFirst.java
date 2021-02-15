package ru.geekbrains.garmatin.lesson8.var1;

import ru.geekbrains.garmatin.lesson8.var1.obstacles.Action;
import ru.geekbrains.garmatin.lesson8.var1.obstacles.Obstacle;
import ru.geekbrains.garmatin.lesson8.var1.obstacles.Track;
import ru.geekbrains.garmatin.lesson8.var1.obstacles.Wall;
import ru.geekbrains.garmatin.lesson8.var1.participants.Cat;
import ru.geekbrains.garmatin.lesson8.var1.participants.Robot;
import ru.geekbrains.garmatin.lesson8.var1.participant_requirement.Participating;
import ru.geekbrains.garmatin.lesson8.var1.participants.Human;

public class LessonEighthVariantFirst {
    public static void main(String[] args) {
        Participating[] participants = getParticipants();
        Obstacle[] obstacles = putObstacles();
        startCompetition(participants, obstacles);
    }

    private static Participating[] getParticipants() {
        Participating[] participants = new Participating[3];

        participants[0] = new Robot(150, 120);
        participants[1] = new Human(100, 50);
        participants[2] = new Cat(30, 80);

        return participants;
    }

    private static Obstacle[] putObstacles() {
        Obstacle[] obstacles = new Obstacle[5];

        obstacles[0] = new Track(30);
        obstacles[1] = new Wall(50);
        obstacles[2] = new Track(50);
        obstacles[3] = new Wall(70);
        obstacles[4] = new Track(100);

        return obstacles;
    }

    private static void startCompetition(Participating[] participants, Obstacle[] obstacles) {

        for (Obstacle obstacle : obstacles) {

            for (Participating participant : participants) {

                if (participant.isDroppedOut()) {
                    continue;
                }

                boolean successfully = obstacle.action(new Action(participant));

                if (successfully) {
                    System.out.println(" и ему удалось преодолеть препятствие!");
                } else {
                    System.out.println(" но не смог преодолеть препятствие. Участник выбывает из соревнования.");
                    participant.setDroppedOut();
                    break;
                }
            }

            System.out.println();
        }
    }
}
