package com.qwerty.mazeagentgame.model;

import com.qwerty.mazeagentgame.util.Constants;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Component
public class Maze {

    private boolean[][] grid;
    private Set<Position> walls;
    private List<Position> dynamicObstacles;
    private final Position start = new Position(1, 1);
    private final Position exit = new Position(Constants.GRID_SIZE - 2, Constants.GRID_SIZE - 2);
    private final Random rand = new Random();

    public Maze() {
        regenerate();
    }

    private void setBorders() {
        IntStream.range(0, Constants.GRID_SIZE).forEach(i -> {
            grid[0][i] = grid[Constants.GRID_SIZE - 1][i] = true;
            grid[i][0] = grid[i][Constants.GRID_SIZE - 1] = true;
        });
    }

    public void regenerate() {
        grid = new boolean[Constants.GRID_SIZE][Constants.GRID_SIZE];
        setBorders();

        IntStream.range(1, Constants.GRID_SIZE - 1)
                .forEach(y -> IntStream.range(1, Constants.GRID_SIZE - 1)
                        .forEach(x -> grid[y][x] = rand.nextDouble() < 0.17)
                );

        grid[start.getY()][start.getX()] = false;
        grid[exit.getY()][exit.getX()] = false;

        ensurePathWithVariation();
        ensurePathIsAlwaysOpen();


        walls = collectWalls();
        initDynamicObstacles();
    }

    private void ensurePathWithVariation() {
        int x = start.getX();
        int y = start.getY();

        while (x < exit.getX() || y < exit.getY()) {
            grid[y][x] = false;
            if (x < exit.getX() && (rand.nextDouble() < 0.6 || y >= exit.getY())) {
                x++;
            } else if (y < exit.getY()) {
                y++;
            }
            if (rand.nextDouble() < 0.30) {
                int wallX = x + (rand.nextBoolean() ? 1 : -1);
                int wallY = y + (rand.nextBoolean() ? 1 : -1);
                if (isValid(new Position(wallX, wallY)) && !new Position(wallX, wallY).equals(exit)) {
                    grid[wallY][wallX] = true;
                }
            }
        }
        grid[exit.getY()][exit.getX()] = false;

        if (!hasPath()) {
            for (int i = 0; i < Constants.GRID_SIZE * 2; i++) {
                int rx = rand.nextInt(1, Constants.GRID_SIZE - 1);
                int ry = rand.nextInt(1, Constants.GRID_SIZE - 1);
                if (!start.equals(new Position(rx, ry)) && !exit.equals(new Position(rx, ry))) {
                    grid[ry][rx] = false;
                }
            }
        }
    }

    private void ensurePathIsAlwaysOpen() {
        while (!hasPath()) {
            List<Position> wallsToRemove = walls.stream().toList();
            Collections.shuffle(wallsToRemove, rand);
            for (Position wall : wallsToRemove) {
                grid[wall.getY()][wall.getX()] = false;
                if (hasPath())
                    return;
            }
        }
    }


    private boolean hasPath() {
        Set<Position> visited = new HashSet<>();
        Queue<Position> queue = new LinkedList<>();
        queue.add(start);

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!queue.isEmpty()) {
            Position current = queue.poll();

            if (current.equals(exit)) return true;
            Arrays.stream(directions)
                    .map(dir -> new Position(current.getX() + dir[0], current.getY() + dir[1]))
                    .filter(this::isValid)
                    .filter(pos -> !grid[pos.getY()][pos.getX()])
                    .filter(visited::add)
                    .forEach(queue::offer);
        }
        return false;
    }

    private Set<Position> collectWalls() {
        return IntStream.range(0, Constants.GRID_SIZE)
                .mapToObj(y -> IntStream.range(0, Constants.GRID_SIZE)
                        .filter(x -> grid[y][x])
                        .mapToObj(x -> new Position(x, y))
                )
                .flatMap(s -> s)
                .collect(Collectors.toSet());
    }

    private void initDynamicObstacles() {
        List<Position> positions = IntStream.range(0, Constants.GRID_SIZE)
                .boxed()
                .flatMap(y -> IntStream.range(0, Constants.GRID_SIZE)
                        .mapToObj(x -> new Position(x, y)))
                .filter(pos -> !grid[pos.getY()][pos.getX()] && !pos.equals(start) && !pos.equals(exit))
                .collect(Collectors.toCollection(ArrayList::new));

        if (positions.isEmpty()) {
            dynamicObstacles = new ArrayList<>();
            return;
        }

        Collections.shuffle(positions, rand);
        dynamicObstacles = positions.subList(0, Math.min(4, positions.size()));
    }

    public void moveDynamicObstacles() {
        List<Position> newPositions = new ArrayList<>();
        Set<Position> occupied = new HashSet<>(dynamicObstacles);

        for (Position pos : dynamicObstacles) {
            List<Position> possibleMoves = Arrays.stream(new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}})
                    .map(dir -> new Position(pos.getX() + dir[0], pos.getY() + dir[1]))
                    .filter(this::isValid)
                    .filter(p -> !grid[p.getY()][p.getX()])
                    .filter(p -> !p.equals(start) && !p.equals(exit))
                    .filter(p -> !occupied.contains(p))
                    .toList();

            Position newPos;
            if (!possibleMoves.isEmpty() && rand.nextDouble() < 0.8) {
                newPos = possibleMoves.get(rand.nextInt(possibleMoves.size()));
            } else {
                newPos = pos;
            }

            newPositions.add(newPos);
            occupied.add(newPos);
        }

        dynamicObstacles = newPositions;
    }

    private boolean isValid(Position pos) {
        return pos.getX() >= 0 && pos.getX() < Constants.GRID_SIZE && pos.getY() >= 0 && pos.getY() < Constants.GRID_SIZE;
    }

}