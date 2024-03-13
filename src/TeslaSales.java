import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

public class TeslaSales {

	public static void main(String[] args) throws IOException {

		Integer[] m3Years = { 2017, 2018, 2019 };
		Integer[] mSYears = { 2016, 2017, 2018, 2019 };
		Integer[] mXYears = { 2016, 2017, 2018, 2019 };

		generateSalesReports(getDataFromFile("model3.csv"), m3Years, "Model 3");
		generateSalesReports(getDataFromFile("modelS.csv"), mSYears, "Model S");
		generateSalesReports(getDataFromFile("modelX.csv"), mXYears, "Model X");
	}

	
	private static Map<YearMonth, Integer> getDataFromFile(String fileName) throws IOException {
		// Read data file and put it into a map
		Map<YearMonth, Integer> salesReport = Files.lines(Paths.get(fileName)).map(c -> c.split(","))
				.filter(c -> c[0].toString().equals("Date") == false)
				.collect(Collectors.toMap(c -> YearMonth.parse(c[0].toString(), DateTimeFormatter.ofPattern("MMM-yy")),
						c -> Integer.parseInt(c[1])));
		return salesReport;
	}

	private static void generateSalesReports(Map<YearMonth, Integer> salesReport, Integer[] years, String carModel) {

		System.out.println(carModel + " Yearly Sales Report");
		System.out.println("--------------------");

		for (Integer i : years) {
			printAnnualSales(salesReport, i);
		}

		System.out.println();

		// Calculate the BEST month for each model
		Optional<Entry<YearMonth, Integer>> maxEntry = salesReport.entrySet().stream().max(
				(Entry<YearMonth, Integer> e1, Entry<YearMonth, Integer> e2) -> e1.getValue().compareTo(e2.getValue()));

		// Calculate the WORST month for each model
		Optional<Entry<YearMonth, Integer>> minEntry = salesReport.entrySet().stream().min(
				(Entry<YearMonth, Integer> e1, Entry<YearMonth, Integer> e2) -> e1.getValue().compareTo(e2.getValue()));

		System.out.println("The best month for " + carModel + " was: " + maxEntry.get().getKey());
		System.out.println("The worst month for " + carModel + " was: " + minEntry.get().getKey());
		System.out.println();
	}

	private static void printAnnualSales(Map<YearMonth, Integer> salesReport, Integer year) {
		// Calculate total number of sales by year
		Integer annualSales = salesReport.entrySet().stream().filter(y -> (y.getKey().getYear()) == year)
				.map(y -> y.getValue()).reduce(0, (a, b) -> a + b);
		System.out.println(year + "->" + annualSales);
	}
}
