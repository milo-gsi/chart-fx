package de.gsi.dataset.spi;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.gsi.dataset.DataSet.DIM_X;
import static de.gsi.dataset.DataSet.DIM_Y;

import org.junit.jupiter.api.Test;

import de.gsi.dataset.event.UpdateEvent;

/**
 * Tests for the DoubleGridDataSet
 * 
 * @author Alexander Krimm
 */
class DoubleGridDataSetTests {
    @Test
    public void testEmptyGridConstructor() {
        DoubleGridDataSet dataset = new DoubleGridDataSet("testGridDataSet", 3);
        assertEquals("testGridDataSet", dataset.getName());
        assertArrayEquals(new int[] { 0, 0 }, dataset.getShape());
        assertEquals(3, dataset.getDimension());
        assertEquals(0, dataset.getDataCount());
    }

    @Test
    public void testZeroInitializedConstructor() {
        DoubleGridDataSet dataset = new DoubleGridDataSet("testGridDataSet", 5, new int[] { 3, 4, 2 });
        assertEquals("testGridDataSet", dataset.getName());
        assertArrayEquals(new int[] { 3, 4, 2 }, dataset.getShape());
        assertEquals(5, dataset.getDimension());
        assertEquals(3 * 4 * 2, dataset.getDataCount());
        assertArrayEquals(new double[] { 0, 1, 2 }, dataset.getGridValues(DIM_X));
        assertEquals(2.0, dataset.getGrid(DIM_Y, 2));
        assertArrayEquals(new double[3 * 4 * 2], dataset.getValues(4));
        assertEquals(0.0, dataset.get(3, 5));
        assertEquals(0.0, dataset.get(3, 2, 3, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.get(3, 25));
        // assertThrows(IndexOutOfBoundsException.class, () -> dataset.get(3, 1, 4, 0));

        assertThrows(IllegalArgumentException.class, () -> new DoubleGridDataSet("testGridDataSet", 2, new int[] { 2, 3, 2 }));
    }

    @Test
    public void testEquidistantFullDataConstructor() {
        double[] data = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        DoubleGridDataSet dataset = new DoubleGridDataSet("testGridDataSet", new int[] { 2, 3, 2 }, data);

        assertEquals("testGridDataSet", dataset.getName());
        assertArrayEquals(new int[] { 2, 3, 2 }, dataset.getShape());
        assertEquals(4, dataset.getDimension());
        assertEquals(2 * 3 * 2, dataset.getDataCount());
        assertArrayEquals(new double[] { 0, 1 }, dataset.getGridValues(DIM_X));
        assertEquals(2.0, dataset.getGrid(DIM_Y, 2));
        assertArrayEquals(data, dataset.getValues(3));
        assertEquals(6.0, dataset.get(3, 5));
        assertEquals(2.0, dataset.get(3, 1, 0, 0));
        assertEquals(12.0, dataset.get(3, 1, 2, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.get(3, 25));
        // assertThrows(IndexOutOfBoundsException.class, () -> dataset.get(3, 1, 4, 0));

        assertThrows(IllegalArgumentException.class, () -> new DoubleGridDataSet("testGridDataSet", new int[] { 2, 3, 2 }, new double[] { 2, 3, 2 }));
    }

    @Test
    public void testFullDataConstructor() {
        double[] data = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        DoubleGridDataSet dataset = new DoubleGridDataSet("testGridDataSet", new double[][] { { 0.1, 0.2 }, { 1.1, 2.2, 3.3 }, { -0.5, 0.5 } }, data);

        assertEquals("testGridDataSet", dataset.getName());
        assertArrayEquals(new int[] { 2, 3, 2 }, dataset.getShape());
        assertEquals(4, dataset.getDimension());
        assertEquals(2 * 3 * 2, dataset.getDataCount());
        assertArrayEquals(new double[] { 0.1, 0.2 }, dataset.getGridValues(DIM_X));
        assertEquals(3.3, dataset.getGrid(DIM_Y, 2));
        assertArrayEquals(data, dataset.getValues(3));
        assertArrayEquals(new double[] { 0.1, 0.2, 0.1, 0.2, 0.1, 0.2, 0.1, 0.2, 0.1, 0.2, 0.1, 0.2 }, dataset.getValues(DIM_X));
        assertEquals(6.0, dataset.get(3, 5));
        assertEquals(0.1, dataset.get(DIM_X, 2));
        assertEquals(2.0, dataset.get(3, 1, 0, 0));
        assertEquals(12.0, dataset.get(3, 1, 2, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.get(3, 25));
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getGrid(2, 25));
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getGrid(3, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getGridValues(3));
        // assertThrows(IndexOutOfBoundsException.class, () -> dataset.get(3, 1, 4, 0));

        assertThrows(IllegalArgumentException.class,
                () -> new DoubleGridDataSet("testGridDataSet", new double[][] { { 0.1, 0.2 }, { 1.1, 2.2, 3.3 }, { -0.5, 0.5 } }, new double[] { 2, 3, 2 }));
    }

    @Test
    public void testCopyConstructor() {
        double[] data = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        DoubleGridDataSet dataset = new DoubleGridDataSet("testGridDataSet", new double[][] { { 0.1, 0.2 }, { 1.1, 2.2, 3.3 }, { -0.5, 0.5 } }, data);

        DoubleGridDataSet datasetCopy = new DoubleGridDataSet(dataset);

        assertEquals(dataset, datasetCopy);
    }

    @Test
    public void testSettersAndListeners() {
        double[] data = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        DoubleGridDataSet dataset = new DoubleGridDataSet("testGridDataSet", new double[][] { { 0.1, 0.2 }, { 1.1, 2.2, 3.3 }, { -0.5, 0.5 } }, data);

        final List<UpdateEvent> events = Collections.synchronizedList(new ArrayList<>(20));
        dataset.addListener((event) -> events.add(event));
        dataset.set(3, new int[] { 1, 2, 1 }, 23.0);
        assertEquals(23.0, dataset.get(3, 1, 2, 1));
        assertSame(dataset, events.get(0).getSource());
        assertEquals("set x_3[1, 2, 1] = 23.0", events.get(0).getMessage());

        assertThrows(UnsupportedOperationException.class, () -> dataset.add(DIM_Y, 2, 4.2, new double[] {1,2,3,4}));
        assertThrows(UnsupportedOperationException.class, () -> dataset.delete(DIM_X, 0));
        assertThrows(UnsupportedOperationException.class, () -> dataset.set(DIM_Y, new int[]{-1,2,-1}, new double[] {1,2,3,4}));
    }
}
