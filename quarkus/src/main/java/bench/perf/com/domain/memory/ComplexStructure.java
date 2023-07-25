package bench.perf.com.domain.memory;

public class ComplexStructure extends SimpleStructure{
    
    ComplexStructure complexStructure;

    public ComplexStructure() {
        this(SimpleStructure.genUUID());
    }

    public ComplexStructure(String uuid) {
        this(uuid, 0);
    }

    public ComplexStructure(String uuid, Integer depth) {
        super(uuid);
        if (Math.random() > 0.5 && depth < 10) {
            this.complexStructure = new ComplexStructure(uuid, depth + 1);
        }
    }
    
}
