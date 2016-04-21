package org.pharmgkb.pharmcat.haplotype;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import com.google.common.collect.Lists;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pharmgkb.pharmcat.haplotype.model.DiplotypeMatch;
import org.pharmgkb.pharmcat.haplotype.model.HaplotypeMatch;


/**
 * @author Mark Woon
 */
public class Haplotype4a4bTest {
  private static List<Haplotype> s_haplotypes;


  @BeforeClass
  public static void beforeClass() {

    // initialize test variants
    Variant s_var1 = new Variant("chr1", "test");
    s_var1.setPosition("1");

    Variant s_var2 = new Variant("chr1", "test");
    s_var2.setPosition("2");

    Variant s_var3 = new Variant("chr1", "test");
    s_var3.setPosition("3");

    List<Variant> s_variants = Lists.newArrayList(s_var1, s_var2, s_var3);

    // initialize test haplotypes
    Haplotype s_hap1 = new Haplotype(
        null, "*1", s_variants,
        null,
        Lists.newArrayList("A", "C", "C")
    );
    s_hap1.calculatePermutations(s_variants);

    Haplotype s_hap2 = new Haplotype(
        null, "*4a", Lists.newArrayList(s_var1),
        null,
        Lists.newArrayList("G")
    );
    s_hap2.calculatePermutations(s_variants);

    Haplotype s_hap3 = new Haplotype(
        null, "*4b", s_variants,
        null,
        Lists.newArrayList("G", "T", "T")
    );
    s_hap3.calculatePermutations(s_variants);

    Haplotype s_hap4 = new Haplotype(
        null, "*17", Lists.newArrayList(s_var2, s_var3),
        null,
        Lists.newArrayList("T", "T")
    );
    s_hap4.calculatePermutations(s_variants);

    s_haplotypes = Lists.newArrayList(s_hap1, s_hap2, s_hap3, s_hap4);
  }


  @Test
  public void test1() throws Exception {

    List<SampleAllele> alleles = Arrays.asList(
        new SampleAllele("chr1", 1, "A", "G", false),
        new SampleAllele("chr1", 2, "C", "C", false),
        new SampleAllele("chr1", 3, "C", "C", false)
    );

    List<DiplotypeMatch> pairMatches = findHaplotypes(alleles);
    ComparisonUtil.printMatchPairs(pairMatches);
  }


  @Test
  public void test2() throws Exception {

    List<SampleAllele> alleles = Arrays.asList(
        new SampleAllele("chr1", 1, "A", "G", false),
        new SampleAllele("chr1", 2, "C", "T", false),
        new SampleAllele("chr1", 3, "C", "T", false)
    );

    List<DiplotypeMatch> pairMatches = findHaplotypes(alleles);
    ComparisonUtil.printMatchPairs(pairMatches);
  }


  @Test
  public void test3() throws Exception {

    List<SampleAllele> alleles = Arrays.asList(
        new SampleAllele("chr1", 1, "A", "A", false),
        new SampleAllele("chr1", 2, "C", "T", false),
        new SampleAllele("chr1", 3, "C", "T", false)
    );

    List<DiplotypeMatch> pairMatches = findHaplotypes(alleles);
    ComparisonUtil.printMatchPairs(pairMatches);
  }


  @Test
  public void test4() throws Exception {

    List<SampleAllele> alleles = Arrays.asList(
        new SampleAllele("chr1", 1, "G", "G", false),
        new SampleAllele("chr1", 2, "T", "T", false),
        new SampleAllele("chr1", 3, "C", "T", false)
    );

    List<DiplotypeMatch> pairMatches = findHaplotypes(alleles);
    ComparisonUtil.printMatchPairs(pairMatches);
  }


  @Test
  public void test5() throws Exception {

    List<SampleAllele> alleles = Arrays.asList(
        new SampleAllele("chr1", 1, "G", "G", false),
        new SampleAllele("chr1", 2, "C", "T", false),
        new SampleAllele("chr1", 3, "C", "T", false)
    );
    List<DiplotypeMatch> pairMatches = findHaplotypes(alleles);
    ComparisonUtil.printMatchPairs(pairMatches);
  }


  private List<DiplotypeMatch> findHaplotypes(List<SampleAllele> alleles) {

    Set<String> permutations = CombinationUtil.generatePermutations(alleles);
    SortedSet<HaplotypeMatch> matches = ComparisonUtil.comparePermutations(permutations, s_haplotypes);

    SortedMap<Integer, SampleAllele> sampleAlleleMap = new TreeMap<>();
    for (SampleAllele sa : alleles) {
      sampleAlleleMap.put(sa.getPosition(), sa);
    }
    return ComparisonUtil.determinePairs(sampleAlleleMap, matches);
  }
}