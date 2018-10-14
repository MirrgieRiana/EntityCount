package mirrg.minecraft.entitycount;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import net.minecraft.world.World;

public class FrameEntityCount
{

	private World world;

	private JFrame frame;
	private TreeSet<EntityEntry> entityEntries = new TreeSet<>((a, b) -> {
		int i;
		i = a.classEntry.clazz.getName().compareTo(b.classEntry.clazz.getName());
		if (i != 0) return i;
		i = a.uuid.compareTo(b.uuid);
		if (i != 0) return i;
		return 0;
	});
	private Timer timer;
	private JTextArea textArea1;
	private JTextArea textArea2;
	private JLabel label;

	public FrameEntityCount(World world)
	{
		this.world = world;

		frame = new JFrame("EntityCount");

		// タイマー関連
		{
			timer = new Timer(5000, e -> {
				measure();
			});
			timer.setRepeats(true);
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e)
				{
					timer.stop();
				}
			});
		}

		// 内容
		{
			frame.setLayout(new BorderLayout());

			// 上部
			{
				JPanel panel = new JPanel();

				{
					panel.setLayout(new FlowLayout());

					{
						JButton button = new JButton("Measure");
						button.addActionListener(e -> {
							measure();
						});
						panel.add(button);
					}

					{
						JToggleButton toggleButton = new JToggleButton("Continuously measure");
						toggleButton.addChangeListener(e -> {
							if (toggleButton.isSelected()) {
								timer.start();
							} else {
								timer.stop();
							}
						});
						panel.add(toggleButton);
					}

					{
						JButton button = new JButton("Clear");
						button.addActionListener(e -> {
							entityEntries.clear();
							update();
						});
						panel.add(button);
					}

				}

				frame.add(panel, BorderLayout.NORTH);
			}

			// 中部
			{
				JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				splitPane.setResizeWeight(0.5);

				{
					textArea1 = new JTextArea();
					textArea1.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
					splitPane.setTopComponent(new JScrollPane(textArea1));
				}

				{
					textArea2 = new JTextArea();
					textArea2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
					splitPane.setBottomComponent(new JScrollPane(textArea2));
				}

				frame.add(splitPane, BorderLayout.CENTER);
			}

			// 下部
			label = new JLabel("...");
			frame.add(label, BorderLayout.SOUTH);

		}

		frame.setSize(600, 600);
		frame.setLocationByPlatform(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	private void measure()
	{
		world.loadedEntityList.stream()
			.forEach(entity -> {
				EntityEntry entityEntry = new EntityEntry(
					entity.getClass(),
					entity.getUniqueID().toString(),
					entity.getName(),
					entity.toString(),
					entity.posX,
					entity.posY,
					entity.posZ);
				entityEntries.add(entityEntry);
			});
		update();
	}

	private void update()
	{

		// 上ペインはすべてのエントリー表示
		textArea1.setText(entityEntries.stream()
			.map(ee -> ee.toString())
			.collect(Collectors.joining("\n")));

		// 下ペインは階層順に個数を表示
		HashMap<ClassEntry, Integer> map = new HashMap<>();
		entityEntries.forEach(ee -> map.put(ee.classEntry, map.getOrDefault(ee.classEntry, 0) + 1));
		textArea2.setText(map.entrySet().stream()
			.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
			.map(e -> "" + e.getKey().clazz.getSimpleName() + "\t" + e.getValue())
			.collect(Collectors.joining("\n")));

		// ラベル更新
		label.setText("Total: " + entityEntries.size());

	}

	public void show()
	{
		frame.setVisible(true);
	}

}
