package pan.alexander.calculator.data;

import javax.inject.Inject;

import pan.alexander.calculator.App;
import pan.alexander.calculator.domain.entities.CalculatorData;
import pan.alexander.calculator.domain.DataRepository;

public class DataRepositoryImplementation implements DataRepository {
    private final RepositoryDao dao = App.getInstance().getDaggerComponent().getRepositoryDao();

    @Inject
    public DataRepositoryImplementation() {
    }

    @Override
    public CalculatorData getSavedData() {
        return dao.getData();
    }

    @Override
    public void saveData(CalculatorData data) {
        dao.saveData(data);
    }

    @Override
    public void clearData() {
        dao.clearData();
    }
}
