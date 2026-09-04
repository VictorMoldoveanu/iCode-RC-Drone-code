import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class BoardState {
    public static final long[] FILE_MASKS = new long[8];
    public static final long[] RANK_MASKS = new long[8];
    
    public static long[] magicRookNumbers;
    public static long[][] rookAttackTable;
    public static long[] magicBishopNumbers;
    public static long[][] bishopAttackTable;
    
    public static long[] ROOK_BLOCKER_MASKS = new long[64];
    public static long[] BISHOP_BLOCKER_MASKS = new long[64];
    
    public static long[] ROOK_XRAY_MASKS = new long[64];
    public static long[] BISHOP_XRAY_MASKS = new long[64];
    
    public static final long[][] BETWEEN = new long[64][64];
    
    public static final long[] KNIGHT_MOVES = new long[64];
    
    // Random numbers for each piece on each square: [piece][square]
 	// piece index: 0 = WP, 1 = WN, ..., 11 = BK
    public static long[] POLYGLOT_ZOBRIST_KEYS = {
		0x9D39247E33776D41L, 0x2AF7398005AAA5C7L, 0x44DB015024623547L, 0x9C15F73E62A76AE2L,
		0x75834465489C0C89L, 0x3290AC3A203001BFL, 0x0FBBAD1F61042279L, 0xE83A908FF2FB60CAL,
		0x0D7E765D58755C10L, 0x1A083822CEAFE02DL, 0x9605D5F0E25EC3B0L, 0xD021FF5CD13A2ED5L,
		0x40BDF15D4A672E32L, 0x011355146FD56395L, 0x5DB4832046F3D9E5L, 0x239F8B2D7FF719CCL,
		0x05D1A1AE85B49AA1L, 0x679F848F6E8FC971L, 0x7449BBFF801FED0BL, 0x7D11CDB1C3B7ADF0L,
		0x82C7709E781EB7CCL, 0xF3218F1C9510786CL, 0x331478F3AF51BBE6L, 0x4BB38DE5E7219443L,
		0xAA649C6EBCFD50FCL, 0x8DBD98A352AFD40BL, 0x87D2074B81D79217L, 0x19F3C751D3E92AE1L,
		0xB4AB30F062B19ABFL, 0x7B0500AC42047AC4L, 0xC9452CA81A09D85DL, 0x24AA6C514DA27500L,
		0x4C9F34427501B447L, 0x14A68FD73C910841L, 0xA71B9B83461CBD93L, 0x03488B95B0F1850FL,
		0x637B2B34FF93C040L, 0x09D1BC9A3DD90A94L, 0x3575668334A1DD3BL, 0x735E2B97A4C45A23L,
		0x18727070F1BD400BL, 0x1FCBACD259BF02E7L, 0xD310A7C2CE9B6555L, 0xBF983FE0FE5D8244L,
		0x9F74D14F7454A824L, 0x51EBDC4AB9BA3035L, 0x5C82C505DB9AB0FAL, 0xFCF7FE8A3430B241L,
		0x3253A729B9BA3DDEL, 0x8C74C368081B3075L, 0xB9BC6C87167C33E7L, 0x7EF48F2B83024E20L,
		0x11D505D4C351BD7FL, 0x6568FCA92C76A243L, 0x4DE0B0F40F32A7B8L, 0x96D693460CC37E5DL,
		0x42E240CB63689F2FL, 0x6D2BDCDAE2919661L, 0x42880B0236E4D951L, 0x5F0F4A5898171BB6L,
		0x39F890F579F92F88L, 0x93C5B5F47356388BL, 0x63DC359D8D231B78L, 0xEC16CA8AEA98AD76L,
		0x5355F900C2A82DC7L, 0x07FB9F855A997142L, 0x5093417AA8A7ED5EL, 0x7BCBC38DA25A7F3CL,
		0x19FC8A768CF4B6D4L, 0x637A7780DECFC0D9L, 0x8249A47AEE0E41F7L, 0x79AD695501E7D1E8L,
		0x14ACBAF4777D5776L, 0xF145B6BECCDEA195L, 0xDABF2AC8201752FCL, 0x24C3C94DF9C8D3F6L,
		0xBB6E2924F03912EAL, 0x0CE26C0B95C980D9L, 0xA49CD132BFBF7CC4L, 0xE99D662AF4243939L,
		0x27E6AD7891165C3FL, 0x8535F040B9744FF1L, 0x54B3F4FA5F40D873L, 0x72B12C32127FED2BL,
		0xEE954D3C7B411F47L, 0x9A85AC909A24EAA1L, 0x70AC4CD9F04F21F5L, 0xF9B89D3E99A075C2L,
		0x87B3E2B2B5C907B1L, 0xA366E5B8C54F48B8L, 0xAE4A9346CC3F7CF2L, 0x1920C04D47267BBDL,
		0x87BF02C6B49E2AE9L, 0x092237AC237F3859L, 0xFF07F64EF8ED14D0L, 0x8DE8DCA9F03CC54EL,
		0x9C1633264DB49C89L, 0xB3F22C3D0B0B38EDL, 0x390E5FB44D01144BL, 0x5BFEA5B4712768E9L,
		0x1E1032911FA78984L, 0x9A74ACB964E78CB3L, 0x4F80F7A035DAFB04L, 0x6304D09A0B3738C4L,
		0x2171E64683023A08L, 0x5B9B63EB9CEFF80CL, 0x506AACF489889342L, 0x1881AFC9A3A701D6L,
		0x6503080440750644L, 0xDFD395339CDBF4A7L, 0xEF927DBCF00C20F2L, 0x7B32F7D1E03680ECL,
		0xB9FD7620E7316243L, 0x05A7E8A57DB91B77L, 0xB5889C6E15630A75L, 0x4A750A09CE9573F7L,
		0xCF464CEC899A2F8AL, 0xF538639CE705B824L, 0x3C79A0FF5580EF7FL, 0xEDE6C87F8477609DL,
		0x799E81F05BC93F31L, 0x86536B8CF3428A8CL, 0x97D7374C60087B73L, 0xA246637CFF328532L,
		0x043FCAE60CC0EBA0L, 0x920E449535DD359EL, 0x70EB093B15B290CCL, 0x73A1921916591CBDL,
		0x56436C9FE1A1AA8DL, 0xEFAC4B70633B8F81L, 0xBB215798D45DF7AFL, 0x45F20042F24F1768L,
		0x930F80F4E8EB7462L, 0xFF6712FFCFD75EA1L, 0xAE623FD67468AA70L, 0xDD2C5BC84BC8D8FCL,
		0x7EED120D54CF2DD9L, 0x22FE545401165F1CL, 0xC91800E98FB99929L, 0x808BD68E6AC10365L,
		0xDEC468145B7605F6L, 0x1BEDE3A3AEF53302L, 0x43539603D6C55602L, 0xAA969B5C691CCB7AL,
		0xA87832D392EFEE56L, 0x65942C7B3C7E11AEL, 0xDED2D633CAD004F6L, 0x21F08570F420E565L,
		0xB415938D7DA94E3CL, 0x91B859E59ECB6350L, 0x10CFF333E0ED804AL, 0x28AED140BE0BB7DDL,
		0xC5CC1D89724FA456L, 0x5648F680F11A2741L, 0x2D255069F0B7DAB3L, 0x9BC5A38EF729ABD4L,
		0xEF2F054308F6A2BCL, 0xAF2042F5CC5C2858L, 0x480412BAB7F5BE2AL, 0xAEF3AF4A563DFE43L,
		0x19AFE59AE451497FL, 0x52593803DFF1E840L, 0xF4F076E65F2CE6F0L, 0x11379625747D5AF3L,
		0xBCE5D2248682C115L, 0x9DA4243DE836994FL, 0x066F70B33FE09017L, 0x4DC4DE189B671A1CL,
		0x51039AB7712457C3L, 0xC07A3F80C31FB4B4L, 0xB46EE9C5E64A6E7CL, 0xB3819A42ABE61C87L,
		0x21A007933A522A20L, 0x2DF16F761598AA4FL, 0x763C4A1371B368FDL, 0xF793C46702E086A0L,
		0xD7288E012AEB8D31L, 0xDE336A2A4BC1C44BL, 0x0BF692B38D079F23L, 0x2C604A7A177326B3L,
		0x4850E73E03EB6064L, 0xCFC447F1E53C8E1BL, 0xB05CA3F564268D99L, 0x9AE182C8BC9474E8L,
		0xA4FC4BD4FC5558CAL, 0xE755178D58FC4E76L, 0x69B97DB1A4C03DFEL, 0xF9B5B7C4ACC67C96L,
		0xFC6A82D64B8655FBL, 0x9C684CB6C4D24417L, 0x8EC97D2917456ED0L, 0x6703DF9D2924E97EL,
		0xC547F57E42A7444EL, 0x78E37644E7CAD29EL, 0xFE9A44E9362F05FAL, 0x08BD35CC38336615L,
		0x9315E5EB3A129ACEL, 0x94061B871E04DF75L, 0xDF1D9F9D784BA010L, 0x3BBA57B68871B59DL,
		0xD2B7ADEEDED1F73FL, 0xF7A255D83BC373F8L, 0xD7F4F2448C0CEB81L, 0xD95BE88CD210FFA7L,
		0x336F52F8FF4728E7L, 0xA74049DAC312AC71L, 0xA2F61BB6E437FDB5L, 0x4F2A5CB07F6A35B3L,
		0x87D380BDA5BF7859L, 0x16B9F7E06C453A21L, 0x7BA2484C8A0FD54EL, 0xF3A678CAD9A2E38CL,
		0x39B0BF7DDE437BA2L, 0xFCAF55C1BF8A4424L, 0x18FCF680573FA594L, 0x4C0563B89F495AC3L,
		0x40E087931A00930DL, 0x8CFFA9412EB642C1L, 0x68CA39053261169FL, 0x7A1EE967D27579E2L,
		0x9D1D60E5076F5B6FL, 0x3810E399B6F65BA2L, 0x32095B6D4AB5F9B1L, 0x35CAB62109DD038AL,
		0xA90B24499FCFAFB1L, 0x77A225A07CC2C6BDL, 0x513E5E634C70E331L, 0x4361C0CA3F692F12L,
		0xD941ACA44B20A45BL, 0x528F7C8602C5807BL, 0x52AB92BEB9613989L, 0x9D1DFA2EFC557F73L,
		0x722FF175F572C348L, 0x1D1260A51107FE97L, 0x7A249A57EC0C9BA2L, 0x04208FE9E8F7F2D6L,
		0x5A110C6058B920A0L, 0x0CD9A497658A5698L, 0x56FD23C8F9715A4CL, 0x284C847B9D887AAEL,
		0x04FEABFBBDB619CBL, 0x742E1E651C60BA83L, 0x9A9632E65904AD3CL, 0x881B82A13B51B9E2L,
		0x506E6744CD974924L, 0xB0183DB56FFC6A79L, 0x0ED9B915C66ED37EL, 0x5E11E86D5873D484L,
		0xF678647E3519AC6EL, 0x1B85D488D0F20CC5L, 0xDAB9FE6525D89021L, 0x0D151D86ADB73615L,
		0xA865A54EDCC0F019L, 0x93C42566AEF98FFBL, 0x99E7AFEABE000731L, 0x48CBFF086DDF285AL,
		0x7F9B6AF1EBF78BAFL, 0x58627E1A149BBA21L, 0x2CD16E2ABD791E33L, 0xD363EFF5F0977996L,
		0x0CE2A38C344A6EEDL, 0x1A804AADB9CFA741L, 0x907F30421D78C5DEL, 0x501F65EDB3034D07L,
		0x37624AE5A48FA6E9L, 0x957BAF61700CFF4EL, 0x3A6C27934E31188AL, 0xD49503536ABCA345L,
		0x088E049589C432E0L, 0xF943AEE7FEBF21B8L, 0x6C3B8E3E336139D3L, 0x364F6FFA464EE52EL,
		0xD60F6DCEDC314222L, 0x56963B0DCA418FC0L, 0x16F50EDF91E513AFL, 0xEF1955914B609F93L,
		0x565601C0364E3228L, 0xECB53939887E8175L, 0xBAC7A9A18531294BL, 0xB344C470397BBA52L,
		0x65D34954DAF3CEBDL, 0xB4B81B3FA97511E2L, 0xB422061193D6F6A7L, 0x071582401C38434DL,
		0x7A13F18BBEDC4FF5L, 0xBC4097B116C524D2L, 0x59B97885E2F2EA28L, 0x99170A5DC3115544L,
		0x6F423357E7C6A9F9L, 0x325928EE6E6F8794L, 0xD0E4366228B03343L, 0x565C31F7DE89EA27L,
		0x30F5611484119414L, 0xD873DB391292ED4FL, 0x7BD94E1D8E17DEBCL, 0xC7D9F16864A76E94L,
		0x947AE053EE56E63CL, 0xC8C93882F9475F5FL, 0x3A9BF55BA91F81CAL, 0xD9A11FBB3D9808E4L,
		0x0FD22063EDC29FCAL, 0xB3F256D8ACA0B0B9L, 0xB03031A8B4516E84L, 0x35DD37D5871448AFL,
		0xE9F6082B05542E4EL, 0xEBFAFA33D7254B59L, 0x9255ABB50D532280L, 0xB9AB4CE57F2D34F3L,
		0x693501D628297551L, 0xC62C58F97DD949BFL, 0xCD454F8F19C5126AL, 0xBBE83F4ECC2BDECBL,
		0xDC842B7E2819E230L, 0xBA89142E007503B8L, 0xA3BC941D0A5061CBL, 0xE9F6760E32CD8021L,
		0x09C7E552BC76492FL, 0x852F54934DA55CC9L, 0x8107FCCF064FCF56L, 0x098954D51FFF6580L,
		0x23B70EDB1955C4BFL, 0xC330DE426430F69DL, 0x4715ED43E8A45C0AL, 0xA8D7E4DAB780A08DL,
		0x0572B974F03CE0BBL, 0xB57D2E985E1419C7L, 0xE8D9ECBE2CF3D73FL, 0x2FE4B17170E59750L,
		0x11317BA87905E790L, 0x7FBF21EC8A1F45ECL, 0x1725CABFCB045B00L, 0x964E915CD5E2B207L,
		0x3E2B8BCBF016D66DL, 0xBE7444E39328A0ACL, 0xF85B2B4FBCDE44B7L, 0x49353FEA39BA63B1L,
		0x1DD01AAFCD53486AL, 0x1FCA8A92FD719F85L, 0xFC7C95D827357AFAL, 0x18A6A990C8B35EBDL,
		0xCCCB7005C6B9C28DL, 0x3BDBB92C43B17F26L, 0xAA70B5B4F89695A2L, 0xE94C39A54A98307FL,
		0xB7A0B174CFF6F36EL, 0xD4DBA84729AF48ADL, 0x2E18BC1AD9704A68L, 0x2DE0966DAF2F8B1CL,
		0xB9C11D5B1E43A07EL, 0x64972D68DEE33360L, 0x94628D38D0C20584L, 0xDBC0D2B6AB90A559L,
		0xD2733C4335C6A72FL, 0x7E75D99D94A70F4DL, 0x6CED1983376FA72BL, 0x97FCAACBF030BC24L,
		0x7B77497B32503B12L, 0x8547EDDFB81CCB94L, 0x79999CDFF70902CBL, 0xCFFE1939438E9B24L,
		0x829626E3892D95D7L, 0x92FAE24291F2B3F1L, 0x63E22C147B9C3403L, 0xC678B6D860284A1CL,
		0x5873888850659AE7L, 0x0981DCD296A8736DL, 0x9F65789A6509A440L, 0x9FF38FED72E9052FL,
		0xE479EE5B9930578CL, 0xE7F28ECD2D49EECDL, 0x56C074A581EA17FEL, 0x5544F7D774B14AEFL,
		0x7B3F0195FC6F290FL, 0x12153635B2C0CF57L, 0x7F5126DBBA5E0CA7L, 0x7A76956C3EAFB413L,
		0x3D5774A11D31AB39L, 0x8A1B083821F40CB4L, 0x7B4A38E32537DF62L, 0x950113646D1D6E03L,
		0x4DA8979A0041E8A9L, 0x3BC36E078F7515D7L, 0x5D0A12F27AD310D1L, 0x7F9D1A2E1EBE1327L,
		0xDA3A361B1C5157B1L, 0xDCDD7D20903D0C25L, 0x36833336D068F707L, 0xCE68341F79893389L,
		0xAB9090168DD05F34L, 0x43954B3252DC25E5L, 0xB438C2B67F98E5E9L, 0x10DCD78E3851A492L,
		0xDBC27AB5447822BFL, 0x9B3CDB65F82CA382L, 0xB67B7896167B4C84L, 0xBFCED1B0048EAC50L,
		0xA9119B60369FFEBDL, 0x1FFF7AC80904BF45L, 0xAC12FB171817EEE7L, 0xAF08DA9177DDA93DL,
		0x1B0CAB936E65C744L, 0xB559EB1D04E5E932L, 0xC37B45B3F8D6F2BAL, 0xC3A9DC228CAAC9E9L,
		0xF3B8B6675A6507FFL, 0x9FC477DE4ED681DAL, 0x67378D8ECCEF96CBL, 0x6DD856D94D259236L,
		0xA319CE15B0B4DB31L, 0x073973751F12DD5EL, 0x8A8E849EB32781A5L, 0xE1925C71285279F5L,
		0x74C04BF1790C0EFEL, 0x4DDA48153C94938AL, 0x9D266D6A1CC0542CL, 0x7440FB816508C4FEL,
		0x13328503DF48229FL, 0xD6BF7BAEE43CAC40L, 0x4838D65F6EF6748FL, 0x1E152328F3318DEAL,
		0x8F8419A348F296BFL, 0x72C8834A5957B511L, 0xD7A023A73260B45CL, 0x94EBC8ABCFB56DAEL,
		0x9FC10D0F989993E0L, 0xDE68A2355B93CAE6L, 0xA44CFE79AE538BBEL, 0x9D1D84FCCE371425L,
		0x51D2B1AB2DDFB636L, 0x2FD7E4B9E72CD38CL, 0x65CA5B96B7552210L, 0xDD69A0D8AB3B546DL,
		0x604D51B25FBF70E2L, 0x73AA8A564FB7AC9EL, 0x1A8C1E992B941148L, 0xAAC40A2703D9BEA0L,
		0x764DBEAE7FA4F3A6L, 0x1E99B96E70A9BE8BL, 0x2C5E9DEB57EF4743L, 0x3A938FEE32D29981L,
		0x26E6DB8FFDF5ADFEL, 0x469356C504EC9F9DL, 0xC8763C5B08D1908CL, 0x3F6C6AF859D80055L,
		0x7F7CC39420A3A545L, 0x9BFB227EBDF4C5CEL, 0x89039D79D6FC5C5CL, 0x8FE88B57305E2AB6L,
		0xA09E8C8C35AB96DEL, 0xFA7E393983325753L, 0xD6B6D0ECC617C699L, 0xDFEA21EA9E7557E3L,
		0xB67C1FA481680AF8L, 0xCA1E3785A9E724E5L, 0x1CFC8BED0D681639L, 0xD18D8549D140CAEAL,
		0x4ED0FE7E9DC91335L, 0xE4DBF0634473F5D2L, 0x1761F93A44D5AEFEL, 0x53898E4C3910DA55L,
		0x734DE8181F6EC39AL, 0x2680B122BAA28D97L, 0x298AF231C85BAFABL, 0x7983EED3740847D5L,
		0x66C1A2A1A60CD889L, 0x9E17E49642A3E4C1L, 0xEDB454E7BADC0805L, 0x50B704CAB602C329L,
		0x4CC317FB9CDDD023L, 0x66B4835D9EAFEA22L, 0x219B97E26FFC81BDL, 0x261E4E4C0A333A9DL,
		0x1FE2CCA76517DB90L, 0xD7504DFA8816EDBBL, 0xB9571FA04DC089C8L, 0x1DDC0325259B27DEL,
		0xCF3F4688801EB9AAL, 0xF4F5D05C10CAB243L, 0x38B6525C21A42B0EL, 0x36F60E2BA4FA6800L,
		0xEB3593803173E0CEL, 0x9C4CD6257C5A3603L, 0xAF0C317D32ADAA8AL, 0x258E5A80C7204C4BL,
		0x8B889D624D44885DL, 0xF4D14597E660F855L, 0xD4347F66EC8941C3L, 0xE699ED85B0DFB40DL,
		0x2472F6207C2D0484L, 0xC2A1E7B5B459AEB5L, 0xAB4F6451CC1D45ECL, 0x63767572AE3D6174L,
		0xA59E0BD101731A28L, 0x116D0016CB948F09L, 0x2CF9C8CA052F6E9FL, 0x0B090A7560A968E3L,
		0xABEEDDB2DDE06FF1L, 0x58EFC10B06A2068DL, 0xC6E57A78FBD986E0L, 0x2EAB8CA63CE802D7L,
		0x14A195640116F336L, 0x7C0828DD624EC390L, 0xD74BBE77E6116AC7L, 0x804456AF10F5FB53L,
		0xEBE9EA2ADF4321C7L, 0x03219A39EE587A30L, 0x49787FEF17AF9924L, 0xA1E9300CD8520548L,
		0x5B45E522E4B1B4EFL, 0xB49C3B3995091A36L, 0xD4490AD526F14431L, 0x12A8F216AF9418C2L,
		0x001F837CC7350524L, 0x1877B51E57A764D5L, 0xA2853B80F17F58EEL, 0x993E1DE72D36D310L,
		0xB3598080CE64A656L, 0x252F59CF0D9F04BBL, 0xD23C8E176D113600L, 0x1BDA0492E7E4586EL,
		0x21E0BD5026C619BFL, 0x3B097ADAF088F94EL, 0x8D14DEDB30BE846EL, 0xF95CFFA23AF5F6F4L,
		0x3871700761B3F743L, 0xCA672B91E9E4FA16L, 0x64C8E531BFF53B55L, 0x241260ED4AD1E87DL,
		0x106C09B972D2E822L, 0x7FBA195410E5CA30L, 0x7884D9BC6CB569D8L, 0x0647DFEDCD894A29L,
		0x63573FF03E224774L, 0x4FC8E9560F91B123L, 0x1DB956E450275779L, 0xB8D91274B9E9D4FBL,
		0xA2EBEE47E2FBFCE1L, 0xD9F1F30CCD97FB09L, 0xEFED53D75FD64E6BL, 0x2E6D02C36017F67FL,
		0xA9AA4D20DB084E9BL, 0xB64BE8D8B25396C1L, 0x70CB6AF7C2D5BCF0L, 0x98F076A4F7A2322EL,
		0xBF84470805E69B5FL, 0x94C3251F06F90CF3L, 0x3E003E616A6591E9L, 0xB925A6CD0421AFF3L,
		0x61BDD1307C66E300L, 0xBF8D5108E27E0D48L, 0x240AB57A8B888B20L, 0xFC87614BAF287E07L,
		0xEF02CDD06FFDB432L, 0xA1082C0466DF6C0AL, 0x8215E577001332C8L, 0xD39BB9C3A48DB6CFL,
		0x2738259634305C14L, 0x61CF4F94C97DF93DL, 0x1B6BACA2AE4E125BL, 0x758F450C88572E0BL,
		0x959F587D507A8359L, 0xB063E962E045F54DL, 0x60E8ED72C0DFF5D1L, 0x7B64978555326F9FL,
		0xFD080D236DA814BAL, 0x8C90FD9B083F4558L, 0x106F72FE81E2C590L, 0x7976033A39F7D952L,
		0xA4EC0132764CA04BL, 0x733EA705FAE4FA77L, 0xB4D8F77BC3E56167L, 0x9E21F4F903B33FD9L,
		0x9D765E419FB69F6DL, 0xD30C088BA61EA5EFL, 0x5D94337FBFAF7F5BL, 0x1A4E4822EB4D7A59L,
		0x6FFE73E81B637FB3L, 0xDDF957BC36D8B9CAL, 0x64D0E29EEA8838B3L, 0x08DD9BDFD96B9F63L,
		0x087E79E5A57D1D13L, 0xE328E230E3E2B3FBL, 0x1C2559E30F0946BEL, 0x720BF5F26F4D2EAAL,
		0xB0774D261CC609DBL, 0x443F64EC5A371195L, 0x4112CF68649A260EL, 0xD813F2FAB7F5C5CAL,
		0x660D3257380841EEL, 0x59AC2C7873F910A3L, 0xE846963877671A17L, 0x93B633ABFA3469F8L,
		0xC0C0F5A60EF4CDCFL, 0xCAF21ECD4377B28CL, 0x57277707199B8175L, 0x506C11B9D90E8B1DL,
		0xD83CC2687A19255FL, 0x4A29C6465A314CD1L, 0xED2DF21216235097L, 0xB5635C95FF7296E2L,
		0x22AF003AB672E811L, 0x52E762596BF68235L, 0x9AEBA33AC6ECC6B0L, 0x944F6DE09134DFB6L,
		0x6C47BEC883A7DE39L, 0x6AD047C430A12104L, 0xA5B1CFDBA0AB4067L, 0x7C45D833AFF07862L,
		0x5092EF950A16DA0BL, 0x9338E69C052B8E7BL, 0x455A4B4CFE30E3F5L, 0x6B02E63195AD0CF8L,
		0x6B17B224BAD6BF27L, 0xD1E0CCD25BB9C169L, 0xDE0C89A556B9AE70L, 0x50065E535A213CF6L,
		0x9C1169FA2777B874L, 0x78EDEFD694AF1EEDL, 0x6DC93D9526A50E68L, 0xEE97F453F06791EDL,
		0x32AB0EDB696703D3L, 0x3A6853C7E70757A7L, 0x31865CED6120F37DL, 0x67FEF95D92607890L,
		0x1F2B1D1F15F6DC9CL, 0xB69E38A8965C6B65L, 0xAA9119FF184CCCF4L, 0xF43C732873F24C13L,
		0xFB4A3D794A9A80D2L, 0x3550C2321FD6109CL, 0x371F77E76BB8417EL, 0x6BFA9AAE5EC05779L,
		0xCD04F3FF001A4778L, 0xE3273522064480CAL, 0x9F91508BFFCFC14AL, 0x049A7F41061A9E60L,
		0xFCB6BE43A9F2FE9BL, 0x08DE8A1C7797DA9BL, 0x8F9887E6078735A1L, 0xB5B4071DBFC73A66L,
		0x230E343DFBA08D33L, 0x43ED7F5A0FAE657DL, 0x3A88A0FBBCB05C63L, 0x21874B8B4D2DBC4FL,
		0x1BDEA12E35F6A8C9L, 0x53C065C6C8E63528L, 0xE34A1D250E7A8D6BL, 0xD6B04D3B7651DD7EL,
		0x5E90277E7CB39E2DL, 0x2C046F22062DC67DL, 0xB10BB459132D0A26L, 0x3FA9DDFB67E2F199L,
		0x0E09B88E1914F7AFL, 0x10E8B35AF3EEAB37L, 0x9EEDECA8E272B933L, 0xD4C718BC4AE8AE5FL,
		0x81536D601170FC20L, 0x91B534F885818A06L, 0xEC8177F83F900978L, 0x190E714FADA5156EL,
		0xB592BF39B0364963L, 0x89C350C893AE7DC1L, 0xAC042E70F8B383F2L, 0xB49B52E587A1EE60L,
		0xFB152FE3FF26DA89L, 0x3E666E6F69AE2C15L, 0x3B544EBE544C19F9L, 0xE805A1E290CF2456L,
		0x24B33C9D7ED25117L, 0xE74733427B72F0C1L, 0x0A804D18B7097475L, 0x57E3306D881EDB4FL,
		0x4AE7D6A36EB5DBCBL, 0x2D8D5432157064C8L, 0xD1E649DE1E7F268BL, 0x8A328A1CEDFE552CL,
		0x07A3AEC79624C7DAL, 0x84547DDC3E203C94L, 0x990A98FD5071D263L, 0x1A4FF12616EEFC89L,
		0xF6F7FD1431714200L, 0x30C05B1BA332F41CL, 0x8D2636B81555A786L, 0x46C9FEB55D120902L,
		0xCCEC0A73B49C9921L, 0x4E9D2827355FC492L, 0x19EBB029435DCB0FL, 0x4659D2B743848A2CL,
		0x963EF2C96B33BE31L, 0x74F85198B05A2E7DL, 0x5A0F544DD2B1FB18L, 0x03727073C2E134B1L,
		0xC7F6AA2DE59AEA61L, 0x352787BAA0D7C22FL, 0x9853EAB63B5E0B35L, 0xABBDCDD7ED5C0860L,
		0xCF05DAF5AC8D77B0L, 0x49CAD48CEBF4A71EL, 0x7A4C10EC2158C4A6L, 0xD9E92AA246BF719EL,
		0x13AE978D09FE5557L, 0x730499AF921549FFL, 0x4E4B705B92903BA4L, 0xFF577222C14F0A3AL,
		0x55B6344CF97AAFAEL, 0xB862225B055B6960L, 0xCAC09AFBDDD2CDB4L, 0xDAF8E9829FE96B5FL,
		0xB5FDFC5D3132C498L, 0x310CB380DB6F7503L, 0xE87FBB46217A360EL, 0x2102AE466EBB1148L,
		0xF8549E1A3AA5E00DL, 0x07A69AFDCC42261AL, 0xC4C118BFE78FEAAEL, 0xF9F4892ED96BD438L,
		0x1AF3DBE25D8F45DAL, 0xF5B4B0B0D2DEEEB4L, 0x962ACEEFA82E1C84L, 0x046E3ECAAF453CE9L,
		0xF05D129681949A4CL, 0x964781CE734B3C84L, 0x9C2ED44081CE5FBDL, 0x522E23F3925E319EL,
		0x177E00F9FC32F791L, 0x2BC60A63A6F3B3F2L, 0x222BBFAE61725606L, 0x486289DDCC3D6780L,
		0x7DC7785B8EFDFC80L, 0x8AF38731C02BA980L, 0x1FAB64EA29A2DDF7L, 0xE4D9429322CD065AL,
		0x9DA058C67844F20CL, 0x24C0E332B70019B0L, 0x233003B5A6CFE6ADL, 0xD586BD01C5C217F6L,
		0x5E5637885F29BC2BL, 0x7EBA726D8C94094BL, 0x0A56A5F0BFE39272L, 0xD79476A84EE20D06L,
		0x9E4C1269BAA4BF37L, 0x17EFEE45B0DEE640L, 0x1D95B0A5FCF90BC6L, 0x93CBE0B699C2585DL,
		0x65FA4F227A2B6D79L, 0xD5F9E858292504D5L, 0xC2B5A03F71471A6FL, 0x59300222B4561E00L,
		0xCE2F8642CA0712DCL, 0x7CA9723FBB2E8988L, 0x2785338347F2BA08L, 0xC61BB3A141E50E8CL,
		0x150F361DAB9DEC26L, 0x9F6A419D382595F4L, 0x64A53DC924FE7AC9L, 0x142DE49FFF7A7C3DL,
		0x0C335248857FA9E7L, 0x0A9C32D5EAE45305L, 0xE6C42178C4BBB92EL, 0x71F1CE2490D20B07L,
		0xF1BCC3D275AFE51AL, 0xE728E8C83C334074L, 0x96FBF83A12884624L, 0x81A1549FD6573DA5L,
		0x5FA7867CAF35E149L, 0x56986E2EF3ED091BL, 0x917F1DD5F8886C61L, 0xD20D8C88C8FFE65FL,
		0x31D71DCE64B2C310L, 0xF165B587DF898190L, 0xA57E6339DD2CF3A0L, 0x1EF6E6DBB1961EC9L,
		0x70CC73D90BC26E24L, 0xE21A6B35DF0C3AD7L, 0x003A93D8B2806962L, 0x1C99DED33CB890A1L,
		0xCF3145DE0ADD4289L, 0xD0E4427A5514FB72L, 0x77C621CC9FB3A483L, 0x67A34DAC4356550BL,
		0xF8D626AAAF278509L
    };
 	
 	public static long[][] ZOBRIST_PIECE_KEYS = new long[12][64];

 	// Random numbers for castling rights: 0b0001 to 0b1000
 	public static long[] ZOBRIST_CASTLING_KEYS = new long[4]; // 0–15 castling combinations

 	// Random numbers for en passant file (0–7)
 	public static long[] ZOBRIST_ENPASSANT_KEYS = new long[8];

 	// Random number for black to move (white to move = 0)
 	public static long ZOBRIST_WHITE_TO_MOVE;
 	
 	public static int moveCount;
    
    static {
    	initializeZobrist();
    	
        for (int from = 0; from < 64; from++) {
            for (int to = 0; to < 64; to++) {
                if (from == to || !areAligned(from, to)) {
                    BETWEEN[from][to] = 0L;
                    continue;
                }
                BETWEEN[from][to] = computeBetween(from, to);
            }
        }
    }
    
    static {
    	for (int pos = 0; pos < 64; pos++) {
            int row = pos / 8;
            int col = pos % 8;
            long mask = 0L;

            // NORTH (exclude edge)
            for (int r = row - 1; r > 0; r--) mask |= (1L << (r * 8 + col));
            // SOUTH
            for (int r = row + 1; r < 7; r++) mask |= (1L << (r * 8 + col));
            // WEST
            for (int c = col - 1; c > 0; c--) mask |= (1L << (row * 8 + c));
            // EAST
            for (int c = col + 1; c < 7; c++) mask |= (1L << (row * 8 + c));

            ROOK_BLOCKER_MASKS[pos] = mask;
        }
    	
    	for (int pos = 0; pos < 64; pos++) {
            int row = pos / 8;
            int col = pos % 8;
            long mask = 0L;

            // NORTH-EAST
            for (int r = row - 1, c = col + 1; r > 0 && c < 7; r--, c++) {
                mask |= (1L << (r * 8 + c));
            }
            // NORTH-WEST
            for (int r = row - 1, c = col - 1; r > 0 && c > 0; r--, c--) {
                mask |= (1L << (r * 8 + c));
            }
            // SOUTH-EAST
            for (int r = row + 1, c = col + 1; r < 7 && c < 7; r++, c++) {
                mask |= (1L << (r * 8 + c));
            }
            // SOUTH-WEST
            for (int r = row + 1, c = col - 1; r < 7 && c > 0; r++, c--) {
                mask |= (1L << (r * 8 + c));
            }

            BISHOP_BLOCKER_MASKS[pos] = mask;
        }
    	
    	for (int pos = 0; pos < 64; pos++) {
            int row = pos / 8;
            int col = pos % 8;
            long mask = 0L;

            // NORTH-EAST
            for (int r = row - 1, c = col + 1; r >= 0 && c <= 7; r--, c++) {
                mask |= (1L << (r * 8 + c));
            }
            // NORTH-WEST
            for (int r = row - 1, c = col - 1; r >= 0 && c >= 0; r--, c--) {
                mask |= (1L << (r * 8 + c));
            }
            // SOUTH-EAST
            for (int r = row + 1, c = col + 1; r <= 7 && c <= 7; r++, c++) {
                mask |= (1L << (r * 8 + c));
            }
            // SOUTH-WEST
            for (int r = row + 1, c = col - 1; r <= 7 && c >= 0; r++, c--) {
                mask |= (1L << (r * 8 + c));
            }

            BISHOP_XRAY_MASKS[pos] = mask;
        }
    	
    }

    static {
        try {
            MagicData magicRookData = MagicLoader.loadFromText("magic_rook_data.txt");
            magicRookNumbers = magicRookData.magicNumbers;
            rookAttackTable = magicRookData.attackTable;

            MagicData magicBishopData = MagicLoader.loadFromText("magic_bishop_data.txt");
            magicBishopNumbers = magicBishopData.magicNumbers;
            bishopAttackTable = magicBishopData.attackTable;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load magic bitboard data", e);
        }
    }

    static {
        // Generate file masks
        for (int file = 0; file < 8; file++) {
            long mask = 0L;
            for (int rank = 0; rank < 8; rank++) {
                mask |= 1L << (rank * 8 + file);
            }
            FILE_MASKS[file] = mask;
        }

        // Generate rank masks
        for (int rank = 0; rank < 8; rank++) {
            long mask = 0L;
            for (int file = 0; file < 8; file++) {
                mask |= 1L << (rank * 8 + file);
            }
            RANK_MASKS[rank] = mask;
        }
        
        for (int i = 0; i < 64; i++) {
    		ROOK_XRAY_MASKS[i] = FILE_MASKS[i%8] ^ RANK_MASKS[i/8];
    	} 
        
        for (int sq = 0; sq < 64; sq++) {
            long knight = 1L << sq;
            long moves = 0L;
            
            long upLeft = (knight >>> 17) & ~FILE_MASKS[7];  // 2 up, 1 left
            moves |= upLeft;
    		
    		long upRight = (knight >>> 15) & ~FILE_MASKS[0];  // 2 up, 1 right
    		moves |= upRight;
    		
    		long upLeft2 = (knight >>> 10) & ~(FILE_MASKS[7] | FILE_MASKS[6]); // 1 up, 2 left
    		moves |= upLeft2;
    		
    		long upRight2 = (knight >>> 6) & ~(FILE_MASKS[0] | FILE_MASKS[1]); // 1 up, 2 right
    		moves |= upRight2;
    		
    		long downRight = (knight << 17) & ~FILE_MASKS[0]; // 2 down, 1 right
    		moves |= downRight;
    		
    		long downLeft = (knight << 15) & ~FILE_MASKS[7]; // 2 down, 1 left
    		moves |= downLeft;
    		
    		long downRight2 = (knight << 10) & ~(FILE_MASKS[0] | FILE_MASKS[1]); // 1 down, 2 right
    		moves |= downRight2;
    		
    		long downLeft2 = (knight << 6) & ~(FILE_MASKS[7] | FILE_MASKS[6]); // 1 down, 2 left
    		moves |= downLeft2;
            KNIGHT_MOVES[sq] = moves;
        }
    }
	
    // 12 piece bitboards:
    public long WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK;
	
    // Occupancy bitboards
    public long whitePieces;
    public long blackPieces;
    public long allPieces;

    // Castling rights: 4 bits packed into one int (WK, WQ, BK, BQ)
    public int castlingRights; // 0b0000 to 0b1111

    // En passant file (0-7 for valid files, -1 if not available)
    public int enPassantFile;

    // Side to move: true = white, false = black
    public boolean whiteToMove;

    // Zobrist hash
    public long zobristHash;

    // History of previous Zobrist hashes (for threefold repetition detection)
    public Set<Long> previousHashes = new HashSet<>();

    // Constructor used for initiating the first board state
    public BoardState(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long whitePieces, long blackPieces, long allPieces, int castlingRights, int enPassantFile, boolean whiteToMove) {
    	this.WP = WP;
    	this.WN = WN;
    	this.WB = WB;
    	this.WR = WR;
    	this.WQ = WQ;
    	this.WK = WK;
    	this.BP = BP;
    	this.BN = BN;
    	this.BB = BB;
    	this.BR = BR;
    	this.BQ = BQ;
    	this.BK = BK;
    	this.whitePieces = whitePieces;
    	this.blackPieces = blackPieces;
    	this.allPieces = allPieces;
    	this.castlingRights = castlingRights;
    	this.enPassantFile = enPassantFile;
    	this.whiteToMove = whiteToMove;
    	this.zobristHash = this.computeZobristHash();
    }
    
    // Copy constructor for cloning the board
    public BoardState(BoardState other) {
    	this.WP = other.WP;
    	this.WN = other.WN;
    	this.WB = other.WB;
    	this.WR = other.WR;
    	this.WQ = other.WQ;
    	this.WK = other.WK;
    	this.BP = other.BP;
    	this.BN = other.BN;
    	this.BB = other.BB;
    	this.BR = other.BR;
    	this.BQ = other.BQ;
    	this.BK = other.BK;
        this.whitePieces = other.whitePieces;
        this.blackPieces = other.blackPieces;
        this.allPieces = other.allPieces;
        this.castlingRights = other.castlingRights;
        this.enPassantFile = other.enPassantFile;
        this.whiteToMove = other.whiteToMove;
        this.zobristHash = other.zobristHash;
        this.previousHashes = new HashSet<>(other.previousHashes);
    }
    
    public long computeZobristHash() {
        long hash = 0L;

        long[] pieces = {BP, WP, BN, WN, BB, WB, BR, WR, BQ, WQ, BK, WK};

        for (int piece = 0; piece < 12; piece++) {
            long bitboard = pieces[piece];
            while (bitboard != 0) {
                int square = Long.numberOfTrailingZeros(bitboard);
                hash ^= ZOBRIST_PIECE_KEYS[piece][square];
                bitboard &= bitboard - 1; // clear LSB
            }
        }

        // Add castling rights
        if ((castlingRights & 0b0100) != 0) {
        	hash ^= ZOBRIST_CASTLING_KEYS[0];
        }
        if ((castlingRights & 0b1000) != 0) {
        	hash ^= ZOBRIST_CASTLING_KEYS[1];
        }
        if ((castlingRights & 0b0001) != 0) {
        	hash ^= ZOBRIST_CASTLING_KEYS[2];
        }
        if ((castlingRights & 0b0010) != 0) {
        	hash ^= ZOBRIST_CASTLING_KEYS[3];
        }

        // Add en passant file if valid
        if (enPassantFile >= 0 && enPassantFile < 8) {
        	if ((enPassantFile == 0) && (whiteToMove) && ((WP & (1L << 17)) != 0))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        	else if ((enPassantFile == 1) && (whiteToMove) && (((WP & (1L << 16)) != 0) || ((WP & (1L << 18)) != 0)))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        	else if ((enPassantFile == 2) && (whiteToMove) && (((WP & (1L << 17)) != 0) || ((WP & (1L << 19)) != 0)))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        	else if ((enPassantFile == 3) && (whiteToMove) && (((WP & (1L << 18)) != 0) || ((WP & (1L << 20)) != 0)))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        	else if ((enPassantFile == 4) && (whiteToMove) && (((WP & (1L << 19)) != 0) || ((WP & (1L << 21)) != 0)))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        	else if ((enPassantFile == 5) && (whiteToMove) && (((WP & (1L << 20)) != 0) || ((WP & (1L << 22)) != 0)))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        	else if ((enPassantFile == 6) && (whiteToMove) && (((WP & (1L << 21)) != 0) || ((WP & (1L << 23)) != 0)))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        	else if ((enPassantFile == 7) && (whiteToMove) && ((WP & (1L << 22)) != 0))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        	else if ((enPassantFile == 0) && ((BP & (1L << 33)) != 0))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        	else if ((enPassantFile == 1) && (((BP & (1L << 32)) != 0) || ((BP & (1L << 34)) != 0)))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        	else if ((enPassantFile == 2) && (((BP & (1L << 33)) != 0) || ((BP & (1L << 35)) != 0)))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        	else if ((enPassantFile == 3) && (((BP & (1L << 34)) != 0) || ((BP & (1L << 36)) != 0)))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        	else if ((enPassantFile == 4) && (((BP & (1L << 35)) != 0) || ((BP & (1L << 37)) != 0)))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        	else if ((enPassantFile == 5) && (((BP & (1L << 36)) != 0) || ((BP & (1L << 38)) != 0)))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        	else if ((enPassantFile == 6) && (((BP & (1L << 37)) != 0) || ((BP & (1L << 39)) != 0)))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        	else if ((enPassantFile == 7) && ((BP & (1L << 38)) != 0))
        		hash ^= ZOBRIST_ENPASSANT_KEYS[enPassantFile];
        }

        // Add side to move (black only)
        if (whiteToMove) {
            hash ^= ZOBRIST_WHITE_TO_MOVE;
        }
        
        return hash;
    }
    
    public static void initializeZobrist() {

        for (int piece = 0; piece < 12; piece++) {
            for (int pos = 0; pos < 64; pos++) {
            	int row = pos/8;
            	int col = pos%8;
                ZOBRIST_PIECE_KEYS[piece][pos] = POLYGLOT_ZOBRIST_KEYS[(piece*64) + (col) + (7-row)*8];
            }
        }
        
        ZOBRIST_CASTLING_KEYS[0] = POLYGLOT_ZOBRIST_KEYS[768];
        ZOBRIST_CASTLING_KEYS[1] = POLYGLOT_ZOBRIST_KEYS[768+1];
        ZOBRIST_CASTLING_KEYS[2] = POLYGLOT_ZOBRIST_KEYS[768+2];
        ZOBRIST_CASTLING_KEYS[3] = POLYGLOT_ZOBRIST_KEYS[768+3];

        for (int i = 0; i < 8; i++) {
            ZOBRIST_ENPASSANT_KEYS[i] = POLYGLOT_ZOBRIST_KEYS[772+i];
        }

        ZOBRIST_WHITE_TO_MOVE = POLYGLOT_ZOBRIST_KEYS[780];
    }
    
    public static int generateLegalMoves(BoardState board, int[] moves){
    	long[] attack_checkers_bishopPin_rookPin_BitBoards = new long[4];
    	int checks = 0;
    	moveCount = 0;
		
    	//white's turn
    	if (board.whiteToMove) {
    		checks = generateForWhite_attack_checkers_bishopPin_rookPin_BitBoards(attack_checkers_bishopPin_rookPin_BitBoards,board);
    		//not in check generate moves
    		if (checks == 0) {
    			if ( (board.enPassantFile != -1) && ((board.WP & ((1L << (board.enPassantFile+25))|(1L << (board.enPassantFile+23)))) != 0) )
    				generateWhitePawnMovesEnPassantPossible(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2], attack_checkers_bishopPin_rookPin_BitBoards[3]);
    			else
    				generateWhitePawnMoves(board, moves,attack_checkers_bishopPin_rookPin_BitBoards[2], attack_checkers_bishopPin_rookPin_BitBoards[3]);
    			generateWhiteKnightMoves(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2]|attack_checkers_bishopPin_rookPin_BitBoards[3]);
    			generateWhiteBishopMoves(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2], attack_checkers_bishopPin_rookPin_BitBoards[3]);
    			generateWhiteRookMoves(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2], attack_checkers_bishopPin_rookPin_BitBoards[3]);
    			generateWhiteQueenMoves(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2], attack_checkers_bishopPin_rookPin_BitBoards[3]);
    			generateWhiteKingMoves(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[0]);
    		}
    		//in check generate moves
    		else if (checks == 1) {
    			if ( (board.enPassantFile != -1) && ((board.WP & ((1L << (board.enPassantFile+25))|(1L << (board.enPassantFile+23)))) != 0) )
    				generateWhitePawnCheckMovesEnPassantPossible(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2]|attack_checkers_bishopPin_rookPin_BitBoards[3], attack_checkers_bishopPin_rookPin_BitBoards[1]);
    			else
    				generateWhitePawnCheckMoves(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2]|attack_checkers_bishopPin_rookPin_BitBoards[3], attack_checkers_bishopPin_rookPin_BitBoards[1]);
    			generateWhiteKnightMovesCheck(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2]|attack_checkers_bishopPin_rookPin_BitBoards[3],attack_checkers_bishopPin_rookPin_BitBoards[1]);
    			generateWhiteBishopMovesCheck(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2]|attack_checkers_bishopPin_rookPin_BitBoards[3],attack_checkers_bishopPin_rookPin_BitBoards[1]);
    			generateWhiteRookMovesCheck(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2]|attack_checkers_bishopPin_rookPin_BitBoards[3],attack_checkers_bishopPin_rookPin_BitBoards[1]);
    			generateWhiteQueenMovesCheck(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2]|attack_checkers_bishopPin_rookPin_BitBoards[3],attack_checkers_bishopPin_rookPin_BitBoards[1]);
    			generateWhiteKingMovesCheck(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[0]);

    		}
    		//double check generate moves
    		else {
    			generateWhiteKingMovesCheck(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[0]);
    		}
    	}
    	
    	//black's turn
    	else {
    		checks = generateForBlack_attack_checkers_bishopPin_rookPin_BitBoards(attack_checkers_bishopPin_rookPin_BitBoards,board);
    		//not in check generate moves
    		if (checks == 0) {
    			if ( (board.enPassantFile != -1) && ((board.BP & ((1L << (board.enPassantFile+33))|(1L << (board.enPassantFile+31)))) != 0) )
    				generateBlackPawnMovesEnPassantPossible(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2], attack_checkers_bishopPin_rookPin_BitBoards[3]);
    			else
    				generateBlackPawnMoves(board, moves,attack_checkers_bishopPin_rookPin_BitBoards[2], attack_checkers_bishopPin_rookPin_BitBoards[3]);
    			generateBlackKnightMoves(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2]|attack_checkers_bishopPin_rookPin_BitBoards[3]);
    			generateBlackBishopMoves(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2], attack_checkers_bishopPin_rookPin_BitBoards[3]);
    			generateBlackRookMoves(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2], attack_checkers_bishopPin_rookPin_BitBoards[3]);
    			generateBlackQueenMoves(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2], attack_checkers_bishopPin_rookPin_BitBoards[3]);
    			generateBlackKingMoves(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[0]);
    		}
    		//in check generate moves
    		else if (checks == 1) {
    			if ( (board.enPassantFile != -1) && ((board.BP & ((1L << (board.enPassantFile+33))|(1L << (board.enPassantFile+31)))) != 0) )
    				generateBlackPawnCheckMovesEnPassantPossible(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2]|attack_checkers_bishopPin_rookPin_BitBoards[3], attack_checkers_bishopPin_rookPin_BitBoards[1]);
    			else
    				generateBlackPawnCheckMoves(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2]|attack_checkers_bishopPin_rookPin_BitBoards[3], attack_checkers_bishopPin_rookPin_BitBoards[1]);
    			generateBlackKnightMovesCheck(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2]|attack_checkers_bishopPin_rookPin_BitBoards[3],attack_checkers_bishopPin_rookPin_BitBoards[1]);
    			generateBlackBishopMovesCheck(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2]|attack_checkers_bishopPin_rookPin_BitBoards[3],attack_checkers_bishopPin_rookPin_BitBoards[1]);
    			generateBlackRookMovesCheck(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2]|attack_checkers_bishopPin_rookPin_BitBoards[3],attack_checkers_bishopPin_rookPin_BitBoards[1]);
    			generateBlackQueenMovesCheck(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[2]|attack_checkers_bishopPin_rookPin_BitBoards[3],attack_checkers_bishopPin_rookPin_BitBoards[1]);
    			generateBlackKingMovesCheck(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[0]);
    		}
    		//double check generate moves
    		else {
    			generateBlackKingMovesCheck(board, moves, attack_checkers_bishopPin_rookPin_BitBoards[0]);
    		}
    	}
    	
    	return checks;
    }
    
    private static int generateForWhite_attack_checkers_bishopPin_rookPin_BitBoards(long[] attack_checkers_bishopPin_rookPin_BitBoards, BoardState board) {
    	int checks = 0;
    	// black pawn attacks
		long attacksSE = (board.BP << 9) & ~FILE_MASKS[0]; // southeast (pos + 7)
		if ((attacksSE & board.WK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.WK >>> 9);
		}
		long attacksSW = (board.BP << 7) & ~FILE_MASKS[7]; // southwest (pos + 9)
		if ((attacksSW & board.WK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.WK >>> 7);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= attacksSE | attacksSW;
		
		// black knight attacks
		long upLeft = (board.BN >>> 17) & ~FILE_MASKS[7];  // 2 up, 1 left
		if ((upLeft & board.WK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.WK << 17);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= upLeft;
		
		long upRight = (board.BN >>> 15) & ~FILE_MASKS[0];  // 2 up, 1 right
		if ((upRight & board.WK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.WK << 15);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= upRight;
		
		long upLeft2 = (board.BN >>> 10) & ~(FILE_MASKS[7] | FILE_MASKS[6]); // 1 up, 2 left
		if ((upLeft2 & board.WK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.WK << 10);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= upLeft2;
		
		long upRight2 = (board.BN >>> 6) & ~(FILE_MASKS[0] | FILE_MASKS[1]); // 1 up, 2 right
		if ((upRight2 & board.WK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.WK << 6);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= upRight2;
		
		long downRight = (board.BN << 17) & ~FILE_MASKS[0]; // 2 down, 1 right
		if ((downRight & board.WK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.WK >>> 17);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= downRight;
		
		long downLeft = (board.BN << 15) & ~FILE_MASKS[7]; // 2 down, 1 left
		if ((downLeft & board.WK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.WK >>> 15);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= downLeft;
		
		long downRight2 = (board.BN << 10) & ~(FILE_MASKS[0] | FILE_MASKS[1]); // 1 down, 2 right
		if ((downRight2 & board.WK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.WK >>> 10);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= downRight2;
		
		long downLeft2 = (board.BN << 6) & ~(FILE_MASKS[7] | FILE_MASKS[6]); // 1 down, 2 left
		if ((downLeft2 & board.WK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.WK >>> 6);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= downLeft2;
		
		// black king attacks
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= (board.BK >>> 8); // top attack
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= (board.BK << 8); // bottom attack
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= ( (board.BK >>> 7) | (board.BK << 1) | (board.BK << 9) ) & ~(FILE_MASKS[0]); // right attacks
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= ( (board.BK << 7) | (board.BK >>> 1) | (board.BK >>> 9) ) & ~(FILE_MASKS[7]); // left attacks
		
		// black bishop attacks
		long BBCopy = board.BB;
		while (BBCopy != 0) {
			int pos = Long.numberOfTrailingZeros(BBCopy);
			BBCopy &= BBCopy - 1;
			
			long blocker = (BISHOP_BLOCKER_MASKS[pos] & board.allPieces & ~board.WK);
			long bishopAttack = bishopAttackTable[pos][(int)((blocker * magicBishopNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(bishopAttackTable[pos].length)))];
			
			if ((bishopAttack & board.WK) != 0) {
				checks++;
				attack_checkers_bishopPin_rookPin_BitBoards[1] |= BETWEEN[pos][Long.numberOfTrailingZeros(board.WK)] | (1L << pos);
			}
			else if ((BISHOP_XRAY_MASKS[pos] & board.WK) != 0) {
				long potentialPin = blocker & BETWEEN[pos][Long.numberOfTrailingZeros(board.WK)];
				if (Long.bitCount(potentialPin) == 1)
					attack_checkers_bishopPin_rookPin_BitBoards[2] |= (BETWEEN[pos][Long.numberOfTrailingZeros(board.WK)] | (1L << pos));
			}
			
			attack_checkers_bishopPin_rookPin_BitBoards[0] |= bishopAttack;
		}
		
		// black rook attacks
		long BRCopy = board.BR;
		while (BRCopy != 0) {
			int pos = Long.numberOfTrailingZeros(BRCopy);
			BRCopy &= BRCopy - 1;
			
			long blocker = (ROOK_BLOCKER_MASKS[pos] & board.allPieces & ~board.WK);
			long rookAttack = rookAttackTable[pos][(int)((blocker * magicRookNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[pos].length)))];
			
			if ((rookAttack & board.WK) != 0) {
				checks++;
				attack_checkers_bishopPin_rookPin_BitBoards[1] |= BETWEEN[pos][Long.numberOfTrailingZeros(board.WK)] | (1L << pos);
			}
			else if ((ROOK_XRAY_MASKS[pos] & board.WK) != 0) {
				long potentialPin = blocker & BETWEEN[pos][Long.numberOfTrailingZeros(board.WK)];
				if (Long.bitCount(potentialPin) == 1)
					attack_checkers_bishopPin_rookPin_BitBoards[3] |= (1L << pos) | BETWEEN[pos][Long.numberOfTrailingZeros(board.WK)];
			}
			
			attack_checkers_bishopPin_rookPin_BitBoards[0] |= rookAttack;
		}
		
		// black queen attacks
		long BQCopy = board.BQ;
		while (BQCopy != 0) {
			int pos = Long.numberOfTrailingZeros(BQCopy);
			BQCopy &= BQCopy - 1;
			
			long rookBlocker = (ROOK_BLOCKER_MASKS[pos] & board.allPieces & ~board.WK);
			long rookAttack = rookAttackTable[pos][(int)((rookBlocker * magicRookNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[pos].length)))];
			long bishopBlocker = (BISHOP_BLOCKER_MASKS[pos] & board.allPieces & ~board.WK);
			long bishopAttack = bishopAttackTable[pos][(int)((bishopBlocker * magicBishopNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(bishopAttackTable[pos].length)))];
			long blocker = rookBlocker | bishopBlocker;
			
			if ( ((rookAttack & board.WK) != 0) || ((bishopAttack & board.WK) != 0) ){
				checks++;
				attack_checkers_bishopPin_rookPin_BitBoards[1] |= BETWEEN[pos][Long.numberOfTrailingZeros(board.WK)] | (1L << pos);
			}
			else if ((ROOK_XRAY_MASKS[pos] & board.WK) != 0) {
				long potentialPin = blocker & BETWEEN[pos][Long.numberOfTrailingZeros(board.WK)];
				if (Long.bitCount(potentialPin) == 1)
					attack_checkers_bishopPin_rookPin_BitBoards[3] |= (1L << pos) | BETWEEN[pos][Long.numberOfTrailingZeros(board.WK)];
			}
			else if ((BISHOP_XRAY_MASKS[pos] & board.WK) != 0) {
				long potentialPin = blocker & BETWEEN[pos][Long.numberOfTrailingZeros(board.WK)];
				if (Long.bitCount(potentialPin) == 1)
					attack_checkers_bishopPin_rookPin_BitBoards[2] |= (1L << pos) | BETWEEN[pos][Long.numberOfTrailingZeros(board.WK)];
			}
			
			attack_checkers_bishopPin_rookPin_BitBoards[0] |= (rookAttack | bishopAttack);
		}
    	return checks;
    }
    private static int generateForBlack_attack_checkers_bishopPin_rookPin_BitBoards(long[] attack_checkers_bishopPin_rookPin_BitBoards, BoardState board) {
    	int checks = 0;
    	// white pawn attacks
		long attacksSE = (board.WP >>> 7) & ~FILE_MASKS[0]; // northeast (pos + 7)
		if ((attacksSE & board.BK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.BK << 7);
		}
		long attacksSW = (board.WP >>> 9) & ~FILE_MASKS[7]; // northwest (pos + 9)
		if ((attacksSW & board.BK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.BK << 9);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= attacksSE | attacksSW;
		
		// white knight attacks
		long upLeft = (board.WN >>> 17) & ~FILE_MASKS[7];  // 2 up, 1 left
		if ((upLeft & board.BK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.BK << 17);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= upLeft;
		
		long upRight = (board.WN >>> 15) & ~FILE_MASKS[0];  // 2 up, 1 right
		if ((upRight & board.BK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.BK << 15);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= upRight;
		
		long upLeft2 = (board.WN >>> 10) & ~(FILE_MASKS[7] | FILE_MASKS[6]); // 1 up, 2 left
		if ((upLeft2 & board.BK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.BK << 10);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= upLeft2;
		
		long upRight2 = (board.WN >>> 6) & ~(FILE_MASKS[0] | FILE_MASKS[1]); // 1 up, 2 right
		if ((upRight2 & board.BK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.BK << 6);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= upRight2;
		
		long downRight = (board.WN << 17) & ~FILE_MASKS[0]; // 2 down, 1 right
		if ((downRight & board.BK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.BK >>> 17);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= downRight;
		
		long downLeft = (board.WN << 15) & ~FILE_MASKS[7]; // 2 down, 1 left
		if ((downLeft & board.BK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.BK >>> 15);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= downLeft;
		
		long downRight2 = (board.WN << 10) & ~(FILE_MASKS[0] | FILE_MASKS[1]); // 1 down, 2 right
		if ((downRight2 & board.BK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.BK >>> 10);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= downRight2;
		
		long downLeft2 = (board.WN << 6) & ~(FILE_MASKS[7] | FILE_MASKS[6]); // 1 down, 2 left
		if ((downLeft2 & board.BK) != 0) {
			checks++;
			attack_checkers_bishopPin_rookPin_BitBoards[1] |= (board.BK >>> 6);
		}
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= downLeft2;
		
		// white king attacks
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= (board.WK >>> 8); // top attack
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= (board.WK << 8); // bottom attack
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= ( (board.WK >>> 7) | (board.WK << 1) | (board.WK << 9) ) & ~(FILE_MASKS[0]); // right attacks
		attack_checkers_bishopPin_rookPin_BitBoards[0] |= ( (board.WK << 7) | (board.WK >>> 1) | (board.WK >>> 9) ) & ~(FILE_MASKS[7]); // left attacks
		
		// white bishop attacks
		long BBCopy = board.WB;
		while (BBCopy != 0) {
			int pos = Long.numberOfTrailingZeros(BBCopy);
			BBCopy &= BBCopy - 1;
			
			long blocker = (BISHOP_BLOCKER_MASKS[pos] & board.allPieces & ~board.BK);
			long bishopAttack = bishopAttackTable[pos][(int)((blocker * magicBishopNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(bishopAttackTable[pos].length)))];
			
			if ((bishopAttack & board.BK) != 0) {
				checks++;
				attack_checkers_bishopPin_rookPin_BitBoards[1] |= BETWEEN[pos][Long.numberOfTrailingZeros(board.BK)] | (1L << pos);
			}
			else if ((BISHOP_XRAY_MASKS[pos] & board.BK) != 0) {
				long potentialPin = blocker & BETWEEN[pos][Long.numberOfTrailingZeros(board.BK)];
				if (Long.bitCount(potentialPin) == 1)
					attack_checkers_bishopPin_rookPin_BitBoards[2] |= (1L << pos) | BETWEEN[pos][Long.numberOfTrailingZeros(board.BK)];
			}
			
			attack_checkers_bishopPin_rookPin_BitBoards[0] |= bishopAttack;
		}
		
		// white rook attacks
		long BRCopy = board.WR;
		while (BRCopy != 0) {
			int pos = Long.numberOfTrailingZeros(BRCopy);
			BRCopy &= BRCopy - 1;
			
			long blocker = (ROOK_BLOCKER_MASKS[pos] & board.allPieces & ~board.BK);
			long rookAttack = rookAttackTable[pos][(int)((blocker * magicRookNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[pos].length)))];
			
			if ((rookAttack & board.BK) != 0) {
				checks++;
				attack_checkers_bishopPin_rookPin_BitBoards[1] |= BETWEEN[pos][Long.numberOfTrailingZeros(board.BK)] | (1L << pos);
			}
			else if ((ROOK_XRAY_MASKS[pos] & board.BK) != 0) {
				long potentialPin = blocker & BETWEEN[pos][Long.numberOfTrailingZeros(board.BK)];
				if (Long.bitCount(potentialPin) == 1)
					attack_checkers_bishopPin_rookPin_BitBoards[3] |= (1L << pos) | BETWEEN[pos][Long.numberOfTrailingZeros(board.BK)];
			}
//			printBitBoard(pinnedPieces);
			
			attack_checkers_bishopPin_rookPin_BitBoards[0] |= rookAttack;
		}
		
		// white queen attacks
		long BQCopy = board.WQ;
		while (BQCopy != 0) {
			int pos = Long.numberOfTrailingZeros(BQCopy);
			BQCopy &= BQCopy - 1;
			
			long rookBlocker = (ROOK_BLOCKER_MASKS[pos] & board.allPieces & ~board.BK);
			long rookAttack = rookAttackTable[pos][(int)((rookBlocker * magicRookNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[pos].length)))];
			long bishopBlocker = (BISHOP_BLOCKER_MASKS[pos] & board.allPieces & ~board.BK);
			long bishopAttack = bishopAttackTable[pos][(int)((bishopBlocker * magicBishopNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(bishopAttackTable[pos].length)))];
			long blocker = rookBlocker | bishopBlocker;
			
			if ( ((rookAttack & board.BK) != 0) || ((bishopAttack & board.BK) != 0) ){
				checks++;
				attack_checkers_bishopPin_rookPin_BitBoards[1] |= BETWEEN[pos][Long.numberOfTrailingZeros(board.BK)] | (1L << pos);
			}
			else if ((ROOK_XRAY_MASKS[pos] & board.BK) != 0) {
				long potentialPin = blocker & BETWEEN[pos][Long.numberOfTrailingZeros(board.BK)];
				if (Long.bitCount(potentialPin) == 1)
					attack_checkers_bishopPin_rookPin_BitBoards[3] |= (1L << pos) | BETWEEN[pos][Long.numberOfTrailingZeros(board.BK)];
			}
			else if ((BISHOP_XRAY_MASKS[pos] & board.BK) != 0) {
				long potentialPin = blocker & BETWEEN[pos][Long.numberOfTrailingZeros(board.BK)];
				if (Long.bitCount(potentialPin) == 1)
					attack_checkers_bishopPin_rookPin_BitBoards[2] |= (1L << pos) | BETWEEN[pos][Long.numberOfTrailingZeros(board.BK)];
			}
			
			attack_checkers_bishopPin_rookPin_BitBoards[0] |= (rookAttack | bishopAttack);
		}
    	return checks;
    }

    
    private static void generateWhitePawnCheckMovesEnPassantPossible(BoardState board, int[] moves, long pinnedPieces, long checkers) {
    	long pawns = board.WP;
    	int enPassantPos = 24 + board.enPassantFile;
    	
    	while (pawns != 0) {
    		int pos = Long.numberOfTrailingZeros(pawns);
			pawns &= pawns - 1;
			long fromBB = (1L<<pos);
			
			// if any piece is pinned and you are in check that piece can't ever move
			if ((fromBB & pinnedPieces) != 0)
				continue;
			
			// white pawn captures checker north east
			if ( (((fromBB >>> 7) & checkers) != 0) && ((fromBB & FILE_MASKS[7]) == 0) ) {
				moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, 0, MoveUtil.PAWN_CAPTURE, 16);
				moveCount++;
			}
			
				
			// white pawn captures checker north west
			if ( (((fromBB >>> 9) & checkers) != 0) && ((fromBB & FILE_MASKS[0]) == 0) ) {
				moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, 0, MoveUtil.PAWN_CAPTURE, 16);
				moveCount++;
			}
			
			if (Long.bitCount(checkers) == 1) {
				// white pawn enPassant north east to get out of check
				if ( (enPassantPos-1 == pos) && (board.enPassantFile > 0) ) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, MoveUtil.EN_PASSANT, MoveUtil.NO_CAPTURE, 16);
					moveCount++;
				}
					
				// white pawn enPassant north west to get out of check
				if ( (enPassantPos+1 == pos) && (board.enPassantFile < 7) ) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, MoveUtil.EN_PASSANT, MoveUtil.NO_CAPTURE, 16);
					moveCount++;			
				}
			}
    	}
    }
    private static void generateWhitePawnCheckMoves(BoardState board, int[] moves, long pinnedPieces, long checkers) {
    	long pawns = board.WP;
    	long pieceOccupiedCheckers = checkers & board.blackPieces;
    	long spaceOccupiedCheckers = pieceOccupiedCheckers ^ checkers;
    	
    	while (pawns != 0) {
    		int pos = Long.numberOfTrailingZeros(pawns);
			pawns &= pawns - 1;
			long fromBB = (1L<<pos);
			
			// if any piece is pinned and you are in check that piece can't ever move
			if	((fromBB & pinnedPieces) != 0)
				continue;
			
			// white pawn moves forward 1 to block check
			if (((fromBB >>> 8) & spaceOccupiedCheckers) != 0) {
				if (((fromBB >>> 8) & RANK_MASKS[0]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-8, MoveUtil.PROMOTE_Q, MoveUtil.NO_CAPTURE, 60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-8, MoveUtil.PROMOTE_N, MoveUtil.NO_CAPTURE, 40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-8, MoveUtil.PROMOTE_R, MoveUtil.NO_CAPTURE, 30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-8, MoveUtil.PROMOTE_B, MoveUtil.NO_CAPTURE, 30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-8, 0, MoveUtil.NO_CAPTURE);
					moveCount++;
				}
			}
				
			// white pawn moves forward 2 to block check
			if ( ((fromBB & RANK_MASKS[6]) != 0) && (((fromBB >>> 16) & spaceOccupiedCheckers) != 0) && ((fromBB >>> 8) & board.allPieces) == 0) {
				moves[moveCount] = MoveUtil.encodeMove(pos, pos-16, MoveUtil.DOUBLE_PAWN_PUSH, MoveUtil.NO_CAPTURE);
				moveCount++;
			}
			
			// white pawn captures checker north east
			if ( (((fromBB >>> 7) & pieceOccupiedCheckers) != 0) && ((fromBB & FILE_MASKS[7]) == 0) ) {
				
				int bonus = 96;
				int capture = MoveUtil.QUEEN_CAPTURE;
				if (((fromBB >>> 7) & board.BP) != 0) {
					capture = MoveUtil.PAWN_CAPTURE;
					bonus = 16;
				}
				else if (((fromBB >>> 7) & board.BN) != 0) {
					capture = MoveUtil.KNIGHT_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB >>> 7) & board.BB) != 0) {
					capture = MoveUtil.BISHOP_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB >>> 7) & board.BR) != 0) {
					capture = MoveUtil.ROOK_CAPTURE;
					bonus = 56;
				}
				
				if (((fromBB >>> 7) & RANK_MASKS[0]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, MoveUtil.PROMOTE_Q, capture, bonus+60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, MoveUtil.PROMOTE_N, capture, bonus+40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, MoveUtil.PROMOTE_R, capture, bonus+30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, MoveUtil.PROMOTE_B, capture, bonus+30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, 0, capture, bonus);
					moveCount++;
				}
			}
			
			// white pawn captures checker north west
			if ( (((fromBB >>> 9) & pieceOccupiedCheckers) != 0) && ((fromBB & FILE_MASKS[0]) == 0) ) {
				int bonus = 96;
				int capture = MoveUtil.QUEEN_CAPTURE;
				if (((fromBB >>> 9) & board.BP) != 0) {
					capture = MoveUtil.PAWN_CAPTURE;
					bonus = 16;
				}
				else if (((fromBB >>> 9) & board.BN) != 0) {
					capture = MoveUtil.KNIGHT_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB >>> 9) & board.BB) != 0) {
					capture = MoveUtil.BISHOP_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB >>> 9) & board.BR) != 0) {
					capture = MoveUtil.ROOK_CAPTURE;
					bonus = 56;
				}
				
				if (((fromBB >>> 9) & RANK_MASKS[0]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, MoveUtil.PROMOTE_Q, capture, bonus+60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, MoveUtil.PROMOTE_N, capture, bonus+40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, MoveUtil.PROMOTE_R, capture, bonus+30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, MoveUtil.PROMOTE_B, capture, bonus+30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, 0, capture, bonus);
					moveCount++;
				}
			}
    	}
    }
    private static void generateWhitePawnMovesEnPassantPossible(BoardState board, int[] moves, long bishopPinnedPieces, long rookPinnedPieces) {
    	long pawns = board.WP;
    	int enPassantPos = 24 + board.enPassantFile;
    	
    	while (pawns != 0) {
    		int pos = Long.numberOfTrailingZeros(pawns);
			pawns &= pawns - 1;
			long fromBB = (1L<<pos);
			
			// white pawn moves forward 1
			if ( (((fromBB >>> 8) & board.allPieces) == 0) && ((fromBB & bishopPinnedPieces) == 0) && ( ((fromBB & rookPinnedPieces) == 0) || (((fromBB >>> 8) & rookPinnedPieces) != 0) ) ) {
				if (((fromBB >>> 8) & RANK_MASKS[0]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-8, MoveUtil.PROMOTE_Q, MoveUtil.NO_CAPTURE, 60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-8, MoveUtil.PROMOTE_N, MoveUtil.NO_CAPTURE, 40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-8, MoveUtil.PROMOTE_R, MoveUtil.NO_CAPTURE, 30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-8, MoveUtil.PROMOTE_B, MoveUtil.NO_CAPTURE, 30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-8, 0, MoveUtil.NO_CAPTURE);
					moveCount++;
				}
			}
			
			// white pawn moves forward 2
			if ( ((fromBB & RANK_MASKS[6]) != 0) && (((fromBB >>> 8) & board.allPieces) == 0) && (((fromBB >>> 16) & board.allPieces) == 0) && ((fromBB & bishopPinnedPieces) == 0) && ( ((fromBB & rookPinnedPieces) == 0) || (((fromBB >>> 16) & rookPinnedPieces) != 0) ) ) { 
				moves[moveCount] = MoveUtil.encodeMove(pos, pos-16, MoveUtil.DOUBLE_PAWN_PUSH, MoveUtil.NO_CAPTURE);
				moveCount++;
			}
			
			// white pawn capture north east
			if ( (((fromBB >>> 7) & board.blackPieces) != 0) && ((fromBB & FILE_MASKS[7]) == 0) && ((fromBB & rookPinnedPieces) == 0) && ( ((fromBB & bishopPinnedPieces) == 0) || (((fromBB >>> 7) & bishopPinnedPieces) != 0) ) ) {
				int bonus = 96;
				int capture = MoveUtil.QUEEN_CAPTURE;
				if (((fromBB >>> 7) & board.BP) != 0) {
					capture = MoveUtil.PAWN_CAPTURE;
					bonus = 16;
				}
				else if (((fromBB >>> 7) & board.BN) != 0) {
					capture = MoveUtil.KNIGHT_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB >>> 7) & board.BB) != 0) {
					capture = MoveUtil.BISHOP_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB >>> 7) & board.BR) != 0) {
					capture = MoveUtil.ROOK_CAPTURE;
					bonus = 56;
				}
				
				if (((fromBB >>> 7) & RANK_MASKS[0]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, MoveUtil.PROMOTE_Q, capture, bonus+60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, MoveUtil.PROMOTE_N, capture, bonus+40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, MoveUtil.PROMOTE_R, capture, bonus+30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, MoveUtil.PROMOTE_B, capture, bonus+30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, 0, capture, bonus);
					moveCount++;
				}
			}
			
			// white pawn capture north west
			if ( (((fromBB >>> 9) & board.blackPieces) != 0) && ((fromBB & FILE_MASKS[0]) == 0) && ((fromBB & rookPinnedPieces) == 0) && ( ((fromBB & bishopPinnedPieces) == 0) || (((fromBB >>> 9) & bishopPinnedPieces) != 0) ) ) {
				int bonus = 96;
				int capture = MoveUtil.QUEEN_CAPTURE;
				if (((fromBB >>> 9) & board.BP) != 0) {
					capture = MoveUtil.PAWN_CAPTURE;
					bonus = 16;
				}
				else if (((fromBB >>> 9) & board.BN) != 0) {
					capture = MoveUtil.KNIGHT_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB >>> 9) & board.BB) != 0) {
					capture = MoveUtil.BISHOP_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB >>> 9) & board.BR) != 0) {
					capture = MoveUtil.ROOK_CAPTURE;
					bonus = 56;
				}
				
				if (((fromBB >>> 9) & RANK_MASKS[0]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, MoveUtil.PROMOTE_Q, capture, bonus+60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, MoveUtil.PROMOTE_N, capture, bonus+40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, MoveUtil.PROMOTE_R, capture, bonus+30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, MoveUtil.PROMOTE_B, capture, bonus+30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, 0, capture, bonus);
					moveCount++;
				}
			}
			
			// white pawn enPassant north east
			if ( (enPassantPos-1 == pos) && (board.enPassantFile > 0) && (((1L << enPassantPos) & bishopPinnedPieces) == 0) && ((fromBB & rookPinnedPieces) == 0) && ( ((fromBB & bishopPinnedPieces) == 0) || (((fromBB >>> 7) & bishopPinnedPieces) != 0) ) ) {
				int move = MoveUtil.encodeMove(pos, pos-7, MoveUtil.EN_PASSANT, MoveUtil.NO_CAPTURE, 16);
				BoardMetaData meta = new BoardMetaData(board.castlingRights,board.enPassantFile);
				BoardState.applyMove(board,move);
				
				int kingPos = Long.numberOfTrailingZeros(board.WK);
				long blocker = (ROOK_BLOCKER_MASKS[kingPos] & board.allPieces);
				long rookAttack = rookAttackTable[kingPos][(int)((blocker * magicRookNumbers[kingPos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[kingPos].length)))];
				
				if ((rookAttack & (board.BR | board.BQ)) == 0) {
					moves[moveCount] = move;
					moveCount++;
				}
				
				BoardState.undoMove(board,move,meta);
			}
			
			// white pawn enPassant north west
			if ( (enPassantPos+1 == pos) && (board.enPassantFile < 7) && (((1L << enPassantPos) & bishopPinnedPieces) == 0) && ((fromBB & rookPinnedPieces) == 0) && ( ((fromBB & bishopPinnedPieces) == 0) || (((fromBB >>> 9) & bishopPinnedPieces) != 0) ) ) {
				int move = MoveUtil.encodeMove(pos, pos-9, MoveUtil.EN_PASSANT, MoveUtil.NO_CAPTURE, 16);
				BoardMetaData meta = new BoardMetaData(board.castlingRights,board.enPassantFile);
				BoardState.applyMove(board,move);
				
				int kingPos = Long.numberOfTrailingZeros(board.WK);
				long blocker = (ROOK_BLOCKER_MASKS[kingPos] & board.allPieces);
				long rookAttack = rookAttackTable[kingPos][(int)((blocker * magicRookNumbers[kingPos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[kingPos].length)))];
				
				if ((rookAttack & (board.BR | board.BQ)) == 0) {
					moves[moveCount] = move;
					moveCount++;
				}
				
				BoardState.undoMove(board,move,meta);
			}
    	}
    }
    private static void generateWhitePawnMoves(BoardState board, int[] moves, long bishopPinnedPieces, long rookPinnedPieces) {
    	long pawns = board.WP;
    	
    	while (pawns != 0) {
    		int pos = Long.numberOfTrailingZeros(pawns);
			pawns &= pawns - 1;
			long fromBB = (1L<<pos);
			
			// white pawn moves forward 1
			if ( (((fromBB >>> 8) & board.allPieces) == 0) && ((fromBB & bishopPinnedPieces) == 0) && ( ((fromBB & rookPinnedPieces) == 0) || (((fromBB >>> 8) & rookPinnedPieces) != 0) ) ) {
				if (((fromBB >>> 8) & RANK_MASKS[0]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-8, MoveUtil.PROMOTE_Q, MoveUtil.NO_CAPTURE, 60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-8, MoveUtil.PROMOTE_N, MoveUtil.NO_CAPTURE, 40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-8, MoveUtil.PROMOTE_R, MoveUtil.NO_CAPTURE, 30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-8, MoveUtil.PROMOTE_B, MoveUtil.NO_CAPTURE, 30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-8, 0, MoveUtil.NO_CAPTURE);
					moveCount++;
				}
			}
			
			// white pawn moves forward 2
			if ( ((fromBB & RANK_MASKS[6]) != 0) && (((fromBB >>> 8) & board.allPieces) == 0) && (((fromBB >>> 16) & board.allPieces) == 0) && ((fromBB & bishopPinnedPieces) == 0) && ( ((fromBB & rookPinnedPieces) == 0) || (((fromBB >>> 8) & rookPinnedPieces) != 0) ) ) {
				moves[moveCount] = MoveUtil.encodeMove(pos, pos-16, MoveUtil.DOUBLE_PAWN_PUSH, MoveUtil.NO_CAPTURE);
				moveCount++;
			}
			
			// white pawn capture north east
			if ( (((fromBB >>> 7) & board.blackPieces) != 0) && ((fromBB & FILE_MASKS[7]) == 0) && ((fromBB & rookPinnedPieces) == 0) && ( ((fromBB & bishopPinnedPieces) == 0) || (((fromBB >>> 7) & bishopPinnedPieces) != 0) ) ) {
				int bonus = 96;
				int capture = MoveUtil.QUEEN_CAPTURE;
				if (((fromBB >>> 7) & board.BP) != 0) {
					capture = MoveUtil.PAWN_CAPTURE;
					bonus = 16;
				}
				else if (((fromBB >>> 7) & board.BN) != 0) {
					capture = MoveUtil.KNIGHT_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB >>> 7) & board.BB) != 0) {
					capture = MoveUtil.BISHOP_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB >>> 7) & board.BR) != 0) {
					capture = MoveUtil.ROOK_CAPTURE;
					bonus = 56;
				}
				
				if (((fromBB >>> 7) & RANK_MASKS[0]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, MoveUtil.PROMOTE_Q, capture, bonus+60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, MoveUtil.PROMOTE_N, capture, bonus+40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, MoveUtil.PROMOTE_R, capture, bonus+30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, MoveUtil.PROMOTE_B, capture, bonus+30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-7, 0, capture, bonus);
					moveCount++;
				}
			}
			
			// white pawn capture north west
			if ( (((fromBB >>> 9) & board.blackPieces) != 0) && ((fromBB & FILE_MASKS[0]) == 0) && ((fromBB & rookPinnedPieces) == 0) && ( ((fromBB & bishopPinnedPieces) == 0) || (((fromBB >>> 9) & bishopPinnedPieces) != 0) ) ) {
				int bonus = 96;
				int capture = MoveUtil.QUEEN_CAPTURE;
				if (((fromBB >>> 9) & board.BP) != 0) {
					capture = MoveUtil.PAWN_CAPTURE;
					bonus = 16;
				}
				else if (((fromBB >>> 9) & board.BN) != 0) {
					capture = MoveUtil.KNIGHT_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB >>> 9) & board.BB) != 0) {
					capture = MoveUtil.BISHOP_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB >>> 9) & board.BR) != 0) {
					capture = MoveUtil.ROOK_CAPTURE;
					bonus = 56;
				}
				
				if (((fromBB >>> 9) & RANK_MASKS[0]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, MoveUtil.PROMOTE_Q, capture, bonus+60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, MoveUtil.PROMOTE_N, capture, bonus+40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, MoveUtil.PROMOTE_R, capture, bonus+30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, MoveUtil.PROMOTE_B, capture, bonus+30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos-9, 0, capture, bonus);
					moveCount++;
				}
			}
    	}
    }
    
    private static void generateBlackPawnCheckMovesEnPassantPossible(BoardState board, int[] moves, long pinnedPieces, long checkers) {
    	long pawns = board.BP;
    	int enPassantPos = 32 + board.enPassantFile;
    	
    	while (pawns != 0) {
    		int pos = Long.numberOfTrailingZeros(pawns);
			pawns &= pawns - 1;
			long fromBB = (1L<<pos);
			
			// if any piece is pinned and you are in check that piece can't ever move
			if ((fromBB & pinnedPieces) != 0)
				continue;
			
			// black pawn captures checker south east
			if ( (((fromBB << 9) & checkers) != 0) && ((fromBB & FILE_MASKS[7]) == 0) ) {
				moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, 0, MoveUtil.PAWN_CAPTURE,16);
				moveCount++;
			}
			
			// black pawn captures checker south west
			if ( (((fromBB << 7) & checkers) != 0) && ((fromBB & FILE_MASKS[0]) == 0) ) {
				moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, 0, MoveUtil.PAWN_CAPTURE,16);
				moveCount++;
			}
			
			if (Long.bitCount(checkers) == 1) {
				// black pawn enPassant south east to get out of check
				if ( (enPassantPos-1 == pos) && (board.enPassantFile > 0) ) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, MoveUtil.EN_PASSANT, MoveUtil.NO_CAPTURE, 16);
					moveCount++;
				}
				
				// black pawn enPassant south west to get out of check
				if ( (enPassantPos+1 == pos) && (board.enPassantFile < 7) ) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, MoveUtil.EN_PASSANT, MoveUtil.NO_CAPTURE, 16);
					moveCount++;
				}
			}
    	}
    }
    private static void generateBlackPawnCheckMoves(BoardState board, int[] moves, long pinnedPieces, long checkers) {
    	long pawns = board.BP;
    	long pieceOccupiedCheckers = checkers & board.whitePieces;
    	long spaceOccupiedCheckers = pieceOccupiedCheckers ^ checkers;
    	
    	while (pawns != 0) {
    		int pos = Long.numberOfTrailingZeros(pawns);
			pawns &= pawns - 1;
			long fromBB = (1L<<pos);
			
			// if any piece is pinned and you are in check that piece can't ever move
			if	((fromBB & pinnedPieces) != 0)
				continue;
			
			// black pawn moves forward 1 to block check
			if (((fromBB << 8) & spaceOccupiedCheckers) != 0) {
				if (((fromBB << 8) & RANK_MASKS[7]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+8, MoveUtil.PROMOTE_Q, MoveUtil.NO_CAPTURE, 60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+8, MoveUtil.PROMOTE_N, MoveUtil.NO_CAPTURE, 40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+8, MoveUtil.PROMOTE_R, MoveUtil.NO_CAPTURE, 30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+8, MoveUtil.PROMOTE_B, MoveUtil.NO_CAPTURE, 30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+8, 0, MoveUtil.NO_CAPTURE);
					moveCount++;
				}
			}
				
			// black pawn moves forward 2 to block check
			if ( ((fromBB & RANK_MASKS[1]) != 0) && (((fromBB << 16) & spaceOccupiedCheckers) != 0) && ((fromBB << 8) & board.allPieces) == 0) {
				moves[moveCount] = MoveUtil.encodeMove(pos, pos+16, MoveUtil.DOUBLE_PAWN_PUSH, MoveUtil.NO_CAPTURE);
				moveCount++;
			}
			
			// black pawn captures checker south east
			if ( (((fromBB << 9) & pieceOccupiedCheckers) != 0) && ((fromBB & FILE_MASKS[7]) == 0) ) {
				int bonus = 96;
				int capture = MoveUtil.QUEEN_CAPTURE;
				if (((fromBB << 9) & board.WP) != 0) {
					capture = MoveUtil.PAWN_CAPTURE;
					bonus = 16;
				}
				else if (((fromBB << 9) & board.WN) != 0) {
					capture = MoveUtil.KNIGHT_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB << 9) & board.WB) != 0) {
					capture = MoveUtil.BISHOP_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB << 9) & board.WR) != 0) {
					capture = MoveUtil.ROOK_CAPTURE;
					bonus = 56;
				}
				
				if (((fromBB << 9) & RANK_MASKS[7]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, MoveUtil.PROMOTE_Q, capture, bonus+60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, MoveUtil.PROMOTE_N, capture, bonus+40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, MoveUtil.PROMOTE_R, capture, bonus+30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, MoveUtil.PROMOTE_B, capture, bonus+30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, 0, capture, bonus);
					moveCount++;
				}
			}
			
			// black pawn captures checker south west
			if ( (((fromBB << 7) & pieceOccupiedCheckers) != 0) && ((fromBB & FILE_MASKS[0]) == 0) ) {
				int bonus = 96;
				int capture = MoveUtil.QUEEN_CAPTURE;
				if (((fromBB << 7) & board.WP) != 0) {
					capture = MoveUtil.PAWN_CAPTURE;
					bonus = 16;
				}
				else if (((fromBB << 7) & board.WN) != 0) {
					capture = MoveUtil.KNIGHT_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB << 7) & board.WB) != 0) {
					capture = MoveUtil.BISHOP_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB << 7) & board.WR) != 0) {
					capture = MoveUtil.ROOK_CAPTURE;
					bonus = 56;
				}
				
				if (((fromBB << 7) & RANK_MASKS[7]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, MoveUtil.PROMOTE_Q, capture, bonus+60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, MoveUtil.PROMOTE_N, capture, bonus+40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, MoveUtil.PROMOTE_R, capture, bonus+30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, MoveUtil.PROMOTE_B, capture, bonus+30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, 0, capture, bonus);
					moveCount++;
				}
			}
    	}
    }
    private static void generateBlackPawnMovesEnPassantPossible(BoardState board, int[] moves, long bishopPinnedPieces, long rookPinnedPieces) {
    	long pawns = board.BP;
    	int enPassantPos = 32 + board.enPassantFile;
    	
    	while (pawns != 0) {
    		int pos = Long.numberOfTrailingZeros(pawns);
			pawns &= pawns - 1;
			long fromBB = (1L<<pos);
						
			// black pawn moves forward 1
			if ( (((fromBB << 8) & board.allPieces) == 0) && ((fromBB & bishopPinnedPieces) == 0) && ( ((fromBB & rookPinnedPieces) == 0) || (((fromBB << 8) & rookPinnedPieces) != 0) ) ) {
				if (((fromBB << 8) & RANK_MASKS[7]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+8, MoveUtil.PROMOTE_Q, MoveUtil.NO_CAPTURE, 60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+8, MoveUtil.PROMOTE_N, MoveUtil.NO_CAPTURE, 40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+8, MoveUtil.PROMOTE_R, MoveUtil.NO_CAPTURE, 30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+8, MoveUtil.PROMOTE_B, MoveUtil.NO_CAPTURE, 30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+8, 0, MoveUtil.NO_CAPTURE);
					moveCount++;
				}
			}
			
			// black pawn moves forward 2
			if ( ((fromBB & RANK_MASKS[1]) != 0) && (((fromBB << 8) & board.allPieces) == 0) && (((fromBB << 16) & board.allPieces) == 0) && ((fromBB & bishopPinnedPieces) == 0) && ( ((fromBB & rookPinnedPieces) == 0) || (((fromBB << 16) & rookPinnedPieces) != 0) ) ) {
				moves[moveCount] = MoveUtil.encodeMove(pos, pos+16, MoveUtil.DOUBLE_PAWN_PUSH, MoveUtil.NO_CAPTURE);
				moveCount++;
			}
			
			// black pawn capture south east
			if ( (((fromBB << 9) & board.whitePieces) != 0) && ((fromBB & FILE_MASKS[7]) == 0) && ((fromBB & rookPinnedPieces) == 0) && ( ((fromBB & bishopPinnedPieces) == 0) || (((fromBB << 9) & bishopPinnedPieces) != 0) ) ) {
				int bonus = 96;
				int capture = MoveUtil.QUEEN_CAPTURE;
				if (((fromBB << 9) & board.WP) != 0) {
					capture = MoveUtil.PAWN_CAPTURE;
					bonus = 16;
				}
				else if (((fromBB << 9) & board.WN) != 0) {
					capture = MoveUtil.KNIGHT_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB << 9) & board.WB) != 0) {
					capture = MoveUtil.BISHOP_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB << 9) & board.WR) != 0) {
					capture = MoveUtil.ROOK_CAPTURE;
					bonus = 56;
				}
				
				if (((fromBB << 9) & RANK_MASKS[7]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, MoveUtil.PROMOTE_Q, capture, bonus+60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, MoveUtil.PROMOTE_N, capture, bonus+40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, MoveUtil.PROMOTE_R, capture, bonus+30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, MoveUtil.PROMOTE_B, capture, bonus+30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, 0, capture, bonus);
					moveCount++;
				}
			}
			
			// black pawn capture south west
			if ( (((fromBB << 7) & board.whitePieces) != 0) && ((fromBB & FILE_MASKS[0]) == 0) && ((fromBB & rookPinnedPieces) == 0) && ( ((fromBB & bishopPinnedPieces) == 0) || (((fromBB << 7) & bishopPinnedPieces) != 0) ) ) {
				int bonus = 96;
				int capture = MoveUtil.QUEEN_CAPTURE;
				if (((fromBB << 7) & board.WP) != 0) {
					capture = MoveUtil.PAWN_CAPTURE;
					bonus = 16;
				}
				else if (((fromBB << 7) & board.WN) != 0) {
					capture = MoveUtil.KNIGHT_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB << 7) & board.WB) != 0) {
					capture = MoveUtil.BISHOP_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB << 7) & board.WR) != 0) {
					capture = MoveUtil.ROOK_CAPTURE;
					bonus = 56;
				}
				
				if (((fromBB << 7) & RANK_MASKS[7]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, MoveUtil.PROMOTE_Q, capture, bonus+60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, MoveUtil.PROMOTE_N, capture, bonus+40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, MoveUtil.PROMOTE_R, capture, bonus+30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, MoveUtil.PROMOTE_B, capture, bonus+30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, 0, capture, bonus);
					moveCount++;
				}
			}
			
			// black pawn enPassant south east
			if ( (enPassantPos-1 == pos) && (board.enPassantFile > 0) && (((1L << enPassantPos) & bishopPinnedPieces) == 0) && ((fromBB & rookPinnedPieces) == 0) && ( ((fromBB & bishopPinnedPieces) == 0) || (((fromBB << 9) & bishopPinnedPieces) != 0) ) ) {		
				int move = MoveUtil.encodeMove(pos, pos+9, MoveUtil.EN_PASSANT, MoveUtil.NO_CAPTURE, 16);
				BoardMetaData meta = new BoardMetaData(board.castlingRights,board.enPassantFile);
				BoardState.applyMove(board,move);
				
				int kingPos = Long.numberOfTrailingZeros(board.BK);
				long blocker = (ROOK_BLOCKER_MASKS[kingPos] & board.allPieces);
				long rookAttack = rookAttackTable[kingPos][(int)((blocker * magicRookNumbers[kingPos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[kingPos].length)))];
				
				if ((rookAttack & (board.WR | board.WQ)) == 0) {
					moves[moveCount] = move;
					moveCount++;
				}
				
				BoardState.undoMove(board,move,meta);
			}
			
			// black pawn enPassant south west
			if ( (enPassantPos+1 == pos) && (board.enPassantFile < 7) && (((1L << enPassantPos) & bishopPinnedPieces) == 0) && ((fromBB & rookPinnedPieces) == 0) && ( ((fromBB & bishopPinnedPieces) == 0) || (((fromBB << 7) & bishopPinnedPieces) != 0) ) ) {
				int move = MoveUtil.encodeMove(pos, pos+7, MoveUtil.EN_PASSANT, MoveUtil.NO_CAPTURE, 16);
				BoardMetaData meta = new BoardMetaData(board.castlingRights,board.enPassantFile);
				BoardState.applyMove(board,move);
				
				int kingPos = Long.numberOfTrailingZeros(board.BK);
				long blocker = (ROOK_BLOCKER_MASKS[kingPos] & board.allPieces);
				long rookAttack = rookAttackTable[kingPos][(int)((blocker * magicRookNumbers[kingPos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[kingPos].length)))];
				
				if ((rookAttack & (board.WR | board.WQ)) == 0) {
					moves[moveCount] = move;
					moveCount++;
				}
				
				BoardState.undoMove(board,move,meta);
			}
    	}
    }
    private static void generateBlackPawnMoves(BoardState board, int[] moves, long bishopPinnedPieces, long rookPinnedPieces) {
    	long pawns = board.BP;
    	
    	while (pawns != 0) {
    		int pos = Long.numberOfTrailingZeros(pawns);
			pawns &= pawns - 1;
			long fromBB = (1L<<pos);
			
			// black pawn moves forward 1
			if ( (((fromBB << 8) & board.allPieces) == 0) && ((fromBB & bishopPinnedPieces) == 0) && ( ((fromBB & rookPinnedPieces) == 0) || (((fromBB << 8) & rookPinnedPieces) != 0) ) ) {
				if (((fromBB << 8) & RANK_MASKS[7]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+8, MoveUtil.PROMOTE_Q, MoveUtil.NO_CAPTURE, 60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+8, MoveUtil.PROMOTE_N, MoveUtil.NO_CAPTURE, 40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+8, MoveUtil.PROMOTE_R, MoveUtil.NO_CAPTURE, 30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+8, MoveUtil.PROMOTE_B, MoveUtil.NO_CAPTURE, 30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+8, 0, MoveUtil.NO_CAPTURE);
					moveCount++;
				}
			}
			
			// black pawn moves forward 2
			if ( ((fromBB & RANK_MASKS[1]) != 0) && (((fromBB << 8) & board.allPieces) == 0) && (((fromBB << 16) & board.allPieces) == 0) && ((fromBB & bishopPinnedPieces) == 0) && ( ((fromBB & rookPinnedPieces) == 0) || (((fromBB << 16) & rookPinnedPieces) != 0) ) ) {
				moves[moveCount] = MoveUtil.encodeMove(pos, pos+16, MoveUtil.DOUBLE_PAWN_PUSH, MoveUtil.NO_CAPTURE);
				moveCount++;
			}
			
			// black pawn capture south east
			if ( (((fromBB << 9) & board.whitePieces) != 0) && ((fromBB & FILE_MASKS[7]) == 0) && ((fromBB & rookPinnedPieces) == 0) && ( ((fromBB & bishopPinnedPieces) == 0) || (((fromBB << 9) & bishopPinnedPieces) != 0) ) ) {
				int bonus = 96;
				int capture = MoveUtil.QUEEN_CAPTURE;
				if (((fromBB << 9) & board.WP) != 0) {
					capture = MoveUtil.PAWN_CAPTURE;
					bonus = 16;
				}
				else if (((fromBB << 9) & board.WN) != 0) {
					capture = MoveUtil.KNIGHT_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB << 9) & board.WB) != 0) {
					capture = MoveUtil.BISHOP_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB << 9) & board.WR) != 0) {
					capture = MoveUtil.ROOK_CAPTURE;
					bonus = 56;
				}
				
				if (((fromBB << 9) & RANK_MASKS[7]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, MoveUtil.PROMOTE_Q, capture, bonus+60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, MoveUtil.PROMOTE_N, capture, bonus+40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, MoveUtil.PROMOTE_R, capture, bonus+30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, MoveUtil.PROMOTE_B, capture, bonus+30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+9, 0, capture, bonus);
					moveCount++;
				}
			}
			
			// black pawn capture south west
			if ( (((fromBB << 7) & board.whitePieces) != 0) && ((fromBB & FILE_MASKS[0]) == 0) && ((fromBB & rookPinnedPieces) == 0) && ( ((fromBB & bishopPinnedPieces) == 0) || (((fromBB << 7) & bishopPinnedPieces) != 0) ) ) {
				int bonus = 96;
				int capture = MoveUtil.QUEEN_CAPTURE;
				if (((fromBB << 7) & board.WP) != 0) {
					capture = MoveUtil.PAWN_CAPTURE;
					bonus = 16;
				}
				else if (((fromBB << 7) & board.WN) != 0) {
					capture = MoveUtil.KNIGHT_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB << 7) & board.WB) != 0) {
					capture = MoveUtil.BISHOP_CAPTURE;
					bonus = 36;
				}
				else if (((fromBB << 7) & board.WR) != 0) {
					capture = MoveUtil.ROOK_CAPTURE;
					bonus = 56;
				}
				
				if (((fromBB << 7) & RANK_MASKS[7]) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, MoveUtil.PROMOTE_Q, capture, bonus+60);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, MoveUtil.PROMOTE_N, capture, bonus+40);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, MoveUtil.PROMOTE_R, capture, bonus+30);
					moveCount++;
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, MoveUtil.PROMOTE_B, capture, bonus+30);
					moveCount++;
				}
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos, pos+7, 0, capture, bonus);
					moveCount++;
				}
			}
    	}
    }

    private static void generateWhiteKnightMoves(BoardState board, int[] moves, long pinnedPieces) {
        long knights = board.WN;
        
        while (knights != 0) {
            int pos = Long.numberOfTrailingZeros(knights);
            knights &= knights - 1;
            long fromBB = 1L << pos;
            
            if ((fromBB & pinnedPieces) != 0) continue;
            
            long knightMoves = KNIGHT_MOVES[pos] & ~board.whitePieces;
            
            while (knightMoves != 0) {
                int toPos = Long.numberOfTrailingZeros(knightMoves);
                long toBB = 1L << toPos;
                knightMoves &= knightMoves - 1;

                if ((toBB & board.blackPieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.BP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,14);
                	moveCount++;
                }
                else if ((toBB & board.BN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,34);
                	moveCount++;
                }
				else if ((toBB & board.BB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,34);
                	moveCount++;       	
				} 
				else if ((toBB & board.BR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,54);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,94);
                	moveCount++; 
				}
            }
        }
    }
    private static void generateWhiteKnightMovesCheck(BoardState board, int[] moves, long pinnedPieces, long checkers) {
    	long knights = board.WN;
        while (knights != 0) {
            int pos = Long.numberOfTrailingZeros(knights);
            knights &= knights - 1;
            long fromBB = 1L << pos;
            
            if ((fromBB & pinnedPieces) != 0) continue;
            
            long knightMoves = KNIGHT_MOVES[pos] & checkers & ~board.whitePieces;
            
            while (knightMoves != 0) {
                int toPos = Long.numberOfTrailingZeros(knightMoves);
                long toBB = 1L << toPos;
                knightMoves &= knightMoves - 1;

                if ((toBB & board.blackPieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.BP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,14);
                	moveCount++;
                }
                else if ((toBB & board.BN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,34);
                	moveCount++;
                }
				else if ((toBB & board.BB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,34);
                	moveCount++;       	
				} 
				else if ((toBB & board.BR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,54);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,94);
                	moveCount++; 
				}
            }
        }
    }
    private static void generateBlackKnightMoves(BoardState board, int[] moves, long pinnedPieces) {
        long knights = board.BN;
        while (knights != 0) {
            int pos = Long.numberOfTrailingZeros(knights);
            long fromBB = 1L << pos;
            knights &= knights - 1;
            
            if ((fromBB & pinnedPieces) != 0) continue;

            long knightMoves = KNIGHT_MOVES[pos] & ~board.blackPieces;
            
            while (knightMoves != 0) {
                int toPos = Long.numberOfTrailingZeros(knightMoves);
                long toBB = 1L << toPos;
                knightMoves &= knightMoves - 1;

                if ((toBB & board.whitePieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.WP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,14);
                	moveCount++;
                }
                else if ((toBB & board.WN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,34);
                	moveCount++;
                }
				else if ((toBB & board.WB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,34);
                	moveCount++;       	
				} 
				else if ((toBB & board.WR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,54);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,94);
                	moveCount++; 
				}
            }
        }
    }
    private static void generateBlackKnightMovesCheck(BoardState board, int[] moves, long pinnedPieces, long checkers) {
    	long knights = board.BN;
        while (knights != 0) {
            int pos = Long.numberOfTrailingZeros(knights);
            long fromBB = 1L << pos;
            knights &= knights - 1;
            
            if ((fromBB & pinnedPieces) != 0) continue;

            long knightMoves = KNIGHT_MOVES[pos] & checkers & ~board.blackPieces;
            
            while (knightMoves != 0) {
                int toPos = Long.numberOfTrailingZeros(knightMoves);
                long toBB = 1L << toPos;
                knightMoves &= knightMoves - 1;

                if ((toBB & board.whitePieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.WP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,14);
                	moveCount++;
                }
                else if ((toBB & board.WN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,34);
                	moveCount++;
                }
				else if ((toBB & board.WB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,34);
                	moveCount++;       	
				} 
				else if ((toBB & board.WR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,54);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,94);
                	moveCount++; 
				}
            }
        }
    }
    
    private static void generateWhiteBishopMoves(BoardState board, int[] moves, long bishopPinnedPieces, long rookPinnedPieces) {
    	long bishops = board.WB;
    	
        while (bishops != 0) {
        	int pos = Long.numberOfTrailingZeros(bishops);
        	bishops &= bishops-1;
        	long fromBB = (1L << pos);
        	
        	if ((fromBB & rookPinnedPieces) != 0)
        		continue;
        	
			long attack = bishopAttackTable[pos][(int)(((BISHOP_BLOCKER_MASKS[pos] & board.allPieces) * magicBishopNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(bishopAttackTable[pos].length)))];
			attack &= ~board.whitePieces;
			
			if ((fromBB & bishopPinnedPieces) != 0) 
				attack &= bishopPinnedPieces;
			
			while (attack != 0) {
				int toPos = Long.numberOfTrailingZeros(attack);
				long toBB = (1L << toPos);
				attack &= attack-1;
				
				if ((toBB & board.blackPieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.BP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,14);
                	moveCount++;
                }
                else if ((toBB & board.BN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,34);
                	moveCount++;
                }
				else if ((toBB & board.BB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,34);
                	moveCount++;       	
				} 
				else if ((toBB & board.BR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,54);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,94);
                	moveCount++; 
				}
			}
        }
    }
    private static void generateWhiteBishopMovesCheck(BoardState board, int[] moves, long pinnedPieces, long checkers) {
    	long bishops = board.WB;
    	
        while (bishops != 0) {
        	int pos = Long.numberOfTrailingZeros(bishops);
        	bishops &= bishops-1;
        	long fromBB = (1L << pos);
        	
        	if ((fromBB & pinnedPieces) != 0)
        		continue;
        	
			long attack = bishopAttackTable[pos][(int)(((BISHOP_BLOCKER_MASKS[pos] & board.allPieces) * magicBishopNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(bishopAttackTable[pos].length)))];
			attack &= ~board.whitePieces;
			attack &= checkers;
			
			while (attack != 0) {
				int toPos = Long.numberOfTrailingZeros(attack);
				long toBB = (1L << toPos);
				attack &= attack-1;
				
				if ((toBB & board.blackPieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.BP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,14);
                	moveCount++;
                }
                else if ((toBB & board.BN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,34);
                	moveCount++;
                }
				else if ((toBB & board.BB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,34);
                	moveCount++;       	
				} 
				else if ((toBB & board.BR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,54);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,94);
                	moveCount++; 
				}
			}
        }
    }
    private static void generateBlackBishopMoves(BoardState board, int[] moves, long bishopPinnedPieces, long rookPinnedPieces) {
    	long bishops = board.BB;
    	
        while (bishops != 0) {
        	int pos = Long.numberOfTrailingZeros(bishops);
        	bishops &= bishops-1;
        	long fromBB = (1L << pos);
        	
        	if ((fromBB & rookPinnedPieces) != 0)
        		continue;
        	
			long attack = bishopAttackTable[pos][(int)(((BISHOP_BLOCKER_MASKS[pos] & board.allPieces) * magicBishopNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(bishopAttackTable[pos].length)))];
			attack &= ~board.blackPieces;
			
			if ((fromBB & bishopPinnedPieces) != 0)
				attack &= bishopPinnedPieces;
			
			while (attack != 0) {
				int toPos = Long.numberOfTrailingZeros(attack);
				long toBB = (1L << toPos);
				attack &= attack-1;
				
				if ((toBB & board.whitePieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.WP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,14);
                	moveCount++;
                }
                else if ((toBB & board.WN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,34);
                	moveCount++;
                }
				else if ((toBB & board.WB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,34);
                	moveCount++;       	
				} 
				else if ((toBB & board.WR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,54);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,94);
                	moveCount++; 
				}
			}
        }
    }
    private static void generateBlackBishopMovesCheck(BoardState board, int[] moves, long pinnedPieces, long checkers) {
    	long bishops = board.BB;
    	
        while (bishops != 0) {
        	int pos = Long.numberOfTrailingZeros(bishops);
        	bishops &= bishops-1;
        	long fromBB = (1L << pos);
        	
        	if ((fromBB & pinnedPieces) != 0)
        		continue;
        	
			long attack = bishopAttackTable[pos][(int)(((BISHOP_BLOCKER_MASKS[pos] & board.allPieces) * magicBishopNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(bishopAttackTable[pos].length)))];
			attack &= ~board.blackPieces;
			attack &= checkers;
			
			while (attack != 0) {
				int toPos = Long.numberOfTrailingZeros(attack);
				long toBB = (1L << toPos);
				attack &= attack-1;
				
				if ((toBB & board.whitePieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.WP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,14);
                	moveCount++;
                }
                else if ((toBB & board.WN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,34);
                	moveCount++;
                }
				else if ((toBB & board.WB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,34);
                	moveCount++;       	
				} 
				else if ((toBB & board.WR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,54);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,94);
                	moveCount++; 
				}
			}
        }
    }
    
    private static void generateWhiteRookMoves(BoardState board, int[] moves, long bishopPinnedPieces, long rookPinnedPieces) {
    	long rooks = board.WR;
    	
        while (rooks != 0) {
        	
        	int pos = Long.numberOfTrailingZeros(rooks);
        	long fromBB = (1L << pos);
        	rooks &= rooks-1;
        	
        	if ((fromBB & bishopPinnedPieces) != 0)
        		continue;
        	
			long attack = rookAttackTable[pos][(int)(((ROOK_BLOCKER_MASKS[pos] & board.allPieces) * magicRookNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[pos].length)))];
			attack &= ~board.whitePieces;
			
			if ((fromBB & rookPinnedPieces) != 0)
				attack &= rookPinnedPieces;
			
			while (attack != 0) {
				int toPos = Long.numberOfTrailingZeros(attack);
				long toBB = (1L << toPos);
				attack &= attack-1;
				
				if ((toBB & board.blackPieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.BP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,12);
                	moveCount++;
                }
                else if ((toBB & board.BN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,32);
                	moveCount++;
                }
				else if ((toBB & board.BB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,32);
                	moveCount++;       	
				} 
				else if ((toBB & board.BR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,52);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,92);
                	moveCount++; 
				}
			}
        }
    }
    private static void generateWhiteRookMovesCheck(BoardState board, int[] moves, long pinnedPieces, long checkers) {
    	long rooks = board.WR;
    	
        while (rooks != 0) {
        	
        	int pos = Long.numberOfTrailingZeros(rooks);
        	long fromBB = (1L << pos);
        	rooks &= rooks-1;
        	
        	if ((fromBB & pinnedPieces) != 0)
        		continue;
        	
			long attack = rookAttackTable[pos][(int)(((ROOK_BLOCKER_MASKS[pos] & board.allPieces) * magicRookNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[pos].length)))];
			attack &= ~board.whitePieces;
			attack &= checkers;
			
			while (attack != 0) {
				int toPos = Long.numberOfTrailingZeros(attack);
				long toBB = (1L << toPos);
				attack &= attack-1;
				
				if ((toBB & board.blackPieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.BP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,12);
                	moveCount++;
                }
                else if ((toBB & board.BN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,32);
                	moveCount++;
                }
				else if ((toBB & board.BB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,32);
                	moveCount++;       	
				} 
				else if ((toBB & board.BR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,52);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,92);
                	moveCount++; 
				}
			}
        }
    }
    private static void generateBlackRookMoves(BoardState board, int[] moves, long bishopPinnedPieces, long rookPinnedPieces) {
    	long rooks = board.BR;
    	
        while (rooks != 0) {
        	int pos = Long.numberOfTrailingZeros(rooks);
        	long fromBB = (1L << pos);
        	rooks &= rooks-1;
        	
        	if ((fromBB & bishopPinnedPieces) != 0)
        		continue;
        	
			long attack = rookAttackTable[pos][(int)(((ROOK_BLOCKER_MASKS[pos] & board.allPieces) * magicRookNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[pos].length)))];
			attack &= ~board.blackPieces;
			
			if ((fromBB & rookPinnedPieces) != 0)
				attack &= rookPinnedPieces;
			
			while (attack != 0) {
				int toPos = Long.numberOfTrailingZeros(attack);
				long toBB = (1L << toPos);
				attack &= attack-1;
				
				if ((toBB & board.whitePieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.WP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,12);
                	moveCount++;
                }
                else if ((toBB & board.WN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,32);
                	moveCount++;
                }
				else if ((toBB & board.WB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,32);
                	moveCount++;       	
				} 
				else if ((toBB & board.WR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,52);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,92);
                	moveCount++; 
				}
			}
        }
    }
    private static void generateBlackRookMovesCheck(BoardState board, int[] moves, long pinnedPieces, long checkers) {
    	long rooks = board.BR;
    	
        while (rooks != 0) {
        	int pos = Long.numberOfTrailingZeros(rooks);
        	long fromBB = (1L << pos);
        	rooks &= rooks-1;
        	
        	if ((fromBB & pinnedPieces) != 0)
        		continue;
        	
			long attack = rookAttackTable[pos][(int)(((ROOK_BLOCKER_MASKS[pos] & board.allPieces) * magicRookNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[pos].length)))];
			attack &= ~board.blackPieces;
			attack &= checkers;
			
			while (attack != 0) {
				int toPos = Long.numberOfTrailingZeros(attack);
				long toBB = (1L << toPos);
				attack &= attack-1;
				
				if ((toBB & board.whitePieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.WP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,12);
                	moveCount++;
                }
                else if ((toBB & board.WN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,32);
                	moveCount++;
                }
				else if ((toBB & board.WB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,32);
                	moveCount++;       	
				} 
				else if ((toBB & board.WR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,52);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,92);
                	moveCount++; 
				}
			}
        }
    }
    
    private static void generateWhiteQueenMoves(BoardState board, int[] moves, long bishopPinnedPieces, long rookPinnedPieces) {
    	long queens = board.WQ;
    	
        while (queens != 0) {
        	int pos = Long.numberOfTrailingZeros(queens);
        	long fromBB = (1L << pos);
        	queens &= queens-1;
        	
        	
			long rookAttack = rookAttackTable[pos][(int)(((ROOK_BLOCKER_MASKS[pos] & board.allPieces) * magicRookNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[pos].length)))];
			long bishopAttack = bishopAttackTable[pos][(int)(((BISHOP_BLOCKER_MASKS[pos] & board.allPieces) * magicBishopNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(bishopAttackTable[pos].length)))];
			
			if ((fromBB & bishopPinnedPieces) != 0) {
				bishopAttack &= bishopPinnedPieces;
				rookAttack = 0L;
			}
			else if ((fromBB & rookPinnedPieces) != 0) {
				bishopAttack = 0L;
				rookAttack &= rookPinnedPieces;
			}
			
			long attack = (rookAttack | bishopAttack) & ~board.whitePieces;

			while (attack != 0) {
				int toPos = Long.numberOfTrailingZeros(attack);
				long toBB = (1L << toPos);
				attack &= attack-1;
				
				if ((toBB & board.blackPieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.BP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,8);
                	moveCount++;
                }
                else if ((toBB & board.BN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,28);
                	moveCount++;
                }
				else if ((toBB & board.BB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,28);
                	moveCount++;       	
				} 
				else if ((toBB & board.BR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,48);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,88);
                	moveCount++; 
				}
			}
        }
    }
    private static void generateWhiteQueenMovesCheck(BoardState board, int[] moves, long pinnedPieces, long checkers) {
    	long queens = board.WQ;
    	
        while (queens != 0) {
        	int pos = Long.numberOfTrailingZeros(queens);
        	long fromBB = (1L << pos);
        	queens &= queens-1;
        	
        	if ((fromBB & pinnedPieces) != 0)
        		continue;
        	
			long rookAttack = rookAttackTable[pos][(int)(((ROOK_BLOCKER_MASKS[pos] & board.allPieces) * magicRookNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[pos].length)))];
			long bishopAttack = bishopAttackTable[pos][(int)(((BISHOP_BLOCKER_MASKS[pos] & board.allPieces) * magicBishopNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(bishopAttackTable[pos].length)))];
			
			long attack = (rookAttack | bishopAttack) & ~board.whitePieces & checkers;

			while (attack != 0) {
				int toPos = Long.numberOfTrailingZeros(attack);
				long toBB = (1L << toPos);
				attack &= attack-1;
				
				if ((toBB & board.blackPieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.BP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,8);
                	moveCount++;
                }
                else if ((toBB & board.BN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,28);
                	moveCount++;
                }
				else if ((toBB & board.BB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,28);
                	moveCount++;       	
				} 
				else if ((toBB & board.BR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,48);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,88);
                	moveCount++; 
				}
			}
        }
    }
    private static void generateBlackQueenMoves(BoardState board, int[] moves, long bishopPinnedPieces, long rookPinnedPieces) {
    	long queens = board.BQ;
    	
        while (queens != 0) {
        	int pos = Long.numberOfTrailingZeros(queens);
        	long fromBB = (1L << pos);
        	queens &= queens-1;
        	
			long rookAttack = rookAttackTable[pos][(int)(((ROOK_BLOCKER_MASKS[pos] & board.allPieces) * magicRookNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[pos].length)))];
			long bishopAttack = bishopAttackTable[pos][(int)(((BISHOP_BLOCKER_MASKS[pos] & board.allPieces) * magicBishopNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(bishopAttackTable[pos].length)))];
			
			if ((fromBB & bishopPinnedPieces) != 0) {
				bishopAttack &= bishopPinnedPieces;
				rookAttack = 0L;
			}
			else if ((fromBB & rookPinnedPieces) != 0) {
				bishopAttack = 0L;
				rookAttack &= rookPinnedPieces;
			}
			
			long attack = (rookAttack | bishopAttack) & ~board.blackPieces;

			while (attack != 0) {
				int toPos = Long.numberOfTrailingZeros(attack);
				long toBB = (1L << toPos);
				attack &= attack-1;
				
				if ((toBB & board.whitePieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.WP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,8);
                	moveCount++;
                }
                else if ((toBB & board.WN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,28);
                	moveCount++;
                }
				else if ((toBB & board.WB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,28);
                	moveCount++;       	
				} 
				else if ((toBB & board.WR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,48);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,88);
                	moveCount++; 
				}
			}
        }
    }
    private static void generateBlackQueenMovesCheck(BoardState board, int[] moves, long pinnedPieces, long checkers) {
    	long queens = board.BQ;
    	
        while (queens != 0) {
        	int pos = Long.numberOfTrailingZeros(queens);
        	long fromBB = (1L << pos);
        	queens &= queens-1;
        	
        	if ((pinnedPieces & fromBB) != 0)
        		continue;
        	
			long rookAttack = rookAttackTable[pos][(int)(((ROOK_BLOCKER_MASKS[pos] & board.allPieces) * magicRookNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(rookAttackTable[pos].length)))];
			long bishopAttack = bishopAttackTable[pos][(int)(((BISHOP_BLOCKER_MASKS[pos] & board.allPieces) * magicBishopNumbers[pos]) >>> (64 - Integer.numberOfTrailingZeros(bishopAttackTable[pos].length)))];
			
			long attack = (rookAttack | bishopAttack) & ~board.blackPieces & checkers;

			while (attack != 0) {
				int toPos = Long.numberOfTrailingZeros(attack);
				long toBB = (1L << toPos);
				attack &= attack-1;
				
				if ((toBB & board.whitePieces) == 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
                	moveCount++;
                }
                else if ((toBB & board.WP) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,8);
                	moveCount++;
                }
                else if ((toBB & board.WN) != 0) {
                	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,28);
                	moveCount++;
                }
				else if ((toBB & board.WB) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,28);
                	moveCount++;       	
				} 
				else if ((toBB & board.WR) != 0) {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,48);
                	moveCount++; 
				} 
				else {
					moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,88);
                	moveCount++; 
				}
			}
        }
    }
    
    private static void generateWhiteKingMoves(BoardState board, int[] moves, long attackBitBoard) {
        long king = board.WK;
        int pos = Long.numberOfTrailingZeros(king);
        long kingMoves = 0L;

        // Generate king attack mask (8 surrounding squares)
        kingMoves |= (king >>> 8); // up
        kingMoves |= (king << 8);  // down

        if ((king & ~FILE_MASKS[0]) != 0) { // not on file A
            kingMoves |= (king >>> 9); // up-left
            kingMoves |= (king >>> 1); // left
            kingMoves |= (king << 7);  // down-left
        }

        if ((king & FILE_MASKS[7]) == 0) { // not on file H
            kingMoves |= (king >>> 7); // up-right
            kingMoves |= (king << 1);  // right
            kingMoves |= (king << 9);  // down-right
        }

        kingMoves &= ~board.whitePieces;
        kingMoves &= ~attackBitBoard;

        // Generate new board states for each legal move
        while (kingMoves != 0) {
            int toPos = Long.numberOfTrailingZeros(kingMoves);
            long toBB = (1L << toPos);
            kingMoves &= kingMoves - 1;

            if ((toBB & board.blackPieces) == 0) {
            	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
            	moveCount++;
            }
            else if ((toBB & board.BP) != 0) {
            	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,5);
            	moveCount++;
            }
            else if ((toBB & board.BN) != 0) {
            	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,25);
            	moveCount++;
            }
			else if ((toBB & board.BB) != 0) {
				moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,25);
            	moveCount++;       	
			} 
			else if ((toBB & board.BR) != 0) {
				moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,45);
            	moveCount++; 
			} 
			else {
				moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,85);
            	moveCount++; 
			}
        }
        
        // White king side castling (e1=60 → g1=62)
        if ((board.castlingRights & 0b0100) != 0 &&
            (board.allPieces & 0x6000000000000000L) == 0 &&           // f1=61, g1=62 empty
            (attackBitBoard & 0x7000000000000000L) == 0 &&
            ((0x8000000000000000L & board.WR) != 0) ){             // e1=60, f1=61, g1=62 not attacked

            moves[moveCount] = MoveUtil.encodeMove(60, 62, MoveUtil.KING_CASTLE, MoveUtil.NO_CAPTURE, 1);
            moveCount++;
        }

        // White queen side castling (e1=60 → c1=58)
        if ((board.castlingRights & 0b1000) != 0 &&
            (board.allPieces & 0x0E00000000000000L) == 0 &&          // b1=57, c1=58, d1=59 empty
            (attackBitBoard & 0x1C00000000000000L) == 0 &&
            ((0x0100000000000000L & board.WR) != 0) ){           // c1=58, d1=59, e1=60 not attacked

        	moves[moveCount] = MoveUtil.encodeMove(60, 58, MoveUtil.QUEEN_CASTLE, MoveUtil.NO_CAPTURE, 1);
            moveCount++;
        }
    }
    private static void generateWhiteKingMovesCheck(BoardState board, int[] moves, long attackBitBoard) {
    	long king = board.WK;
        int pos = Long.numberOfTrailingZeros(king);
        long kingMoves = 0L;

        // Generate king attack mask (8 surrounding squares)
        kingMoves |= (king >>> 8); // up
        kingMoves |= (king << 8);  // down

        if ((king & ~FILE_MASKS[0]) != 0) { // not on file A
            kingMoves |= (king >>> 9); // up-left
            kingMoves |= (king >>> 1); // left
            kingMoves |= (king << 7);  // down-left
        }

        if ((king & FILE_MASKS[7]) == 0) { // not on file H
            kingMoves |= (king >>> 7); // up-right
            kingMoves |= (king << 1);  // right
            kingMoves |= (king << 9);  // down-right
        }

        kingMoves &= ~board.whitePieces;
        kingMoves &= ~attackBitBoard;

        // Generate new board states for each legal move
        while (kingMoves != 0) {
        	int toPos = Long.numberOfTrailingZeros(kingMoves);
            long toBB = (1L << toPos);
            kingMoves &= kingMoves - 1;

            if ((toBB & board.blackPieces) == 0) {
            	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
            	moveCount++;
            }
            else if ((toBB & board.BP) != 0) {
            	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,5);
            	moveCount++;
            }
            else if ((toBB & board.BN) != 0) {
            	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,25);
            	moveCount++;
            }
			else if ((toBB & board.BB) != 0) {
				moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,25);
            	moveCount++;       	
			} 
			else if ((toBB & board.BR) != 0) {
				moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,45);
            	moveCount++; 
			} 
			else {
				moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,85);
            	moveCount++; 
			}
        }
    }
    private static void generateBlackKingMoves(BoardState board, int[] moves, long attackBitBoard) {
        long king = board.BK;
        int pos = Long.numberOfTrailingZeros(king);
        long kingMoves = 0L;

        // Generate king attack mask (8 surrounding squares)
        kingMoves |= (king >>> 8); // up
        kingMoves |= (king << 8);  // down

        if ((king & ~FILE_MASKS[0]) != 0) { // not on file A
            kingMoves |= (king >>> 9); // up-left
            kingMoves |= (king >>> 1); // left
            kingMoves |= (king << 7);  // down-left
        }

        if ((king & FILE_MASKS[7]) == 0) { // not on file H
            kingMoves |= (king >>> 7); // up-right
            kingMoves |= (king << 1);  // right
            kingMoves |= (king << 9);  // down-right
        }

        kingMoves &= ~board.blackPieces;
        kingMoves &= ~attackBitBoard;

        // Generate new board states for each legal move
        while (kingMoves != 0) {
        	int toPos = Long.numberOfTrailingZeros(kingMoves);
            long toBB = (1L << toPos);
            kingMoves &= kingMoves - 1;

            if ((toBB & board.whitePieces) == 0) {
            	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
            	moveCount++;
            }
            else if ((toBB & board.WP) != 0) {
            	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,5);
            	moveCount++;
            }
            else if ((toBB & board.WN) != 0) {
            	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,25);
            	moveCount++;
            }
			else if ((toBB & board.WB) != 0) {
				moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,25);
            	moveCount++;       	
			} 
			else if ((toBB & board.WR) != 0) {
				moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,45);
            	moveCount++; 
			} 
			else {
				moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,85);
            	moveCount++; 
			}
        }
        
        // Black king side castling (e8=4 → g8=6)
        if ((board.castlingRights & 0b0001) != 0 &&
            (board.allPieces & 0x60L) == 0 &&                 // f8=5, g8=6 empty
            (attackBitBoard & 0x70L) == 0 &&
            ((0x0000000000000080L & board.BR) != 0) ){                  // e8=4, f8=5, g8=6 not attacked

        	moves[moveCount] = MoveUtil.encodeMove(4, 6, MoveUtil.KING_CASTLE, MoveUtil.NO_CAPTURE, 1);
            moveCount++;
        }

        // Black queen side castling (e8=4 → c8=2)
        if ((board.castlingRights & 0b0010) != 0 &&
            (board.allPieces & 0xEL) == 0 &&                  // b8=1, c8=2, d8=3 empty
            (attackBitBoard & 0x1CL) == 0 &&
            ((0x0000000000000001L & board.BR) != 0) ) {                  // c8=2, d8=3, e8=4 not attacked
        	
        	moves[moveCount] = MoveUtil.encodeMove(4, 2, MoveUtil.QUEEN_CASTLE, MoveUtil.NO_CAPTURE, 1);
            moveCount++;
        }
    }
    private static void generateBlackKingMovesCheck(BoardState board, int[] moves, long attackBitBoard) {
    	long king = board.BK;
        int pos = Long.numberOfTrailingZeros(king);
        long kingMoves = 0L;

        // Generate king attack mask (8 surrounding squares)
        kingMoves |= (king >>> 8); // up
        kingMoves |= (king << 8);  // down

        if ((king & ~FILE_MASKS[0]) != 0) { // not on file A
            kingMoves |= (king >>> 9); // up-left
            kingMoves |= (king >>> 1); // left
            kingMoves |= (king << 7);  // down-left
        }

        if ((king & FILE_MASKS[7]) == 0) { // not on file H
            kingMoves |= (king >>> 7); // up-right
            kingMoves |= (king << 1);  // right
            kingMoves |= (king << 9);  // down-right
        }

        kingMoves &= ~board.blackPieces;
        kingMoves &= ~attackBitBoard;

        // Generate new board states for each legal move
        while (kingMoves != 0) {
        	int toPos = Long.numberOfTrailingZeros(kingMoves);
            long toBB = (1L << toPos);
            kingMoves &= kingMoves - 1;

            if ((toBB & board.whitePieces) == 0) {
            	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.NO_CAPTURE);
            	moveCount++;
            }
            else if ((toBB & board.WP) != 0) {
            	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.PAWN_CAPTURE,5);
            	moveCount++;
            }
            else if ((toBB & board.WN) != 0) {
            	moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.KNIGHT_CAPTURE,25);
            	moveCount++;
            }
			else if ((toBB & board.WB) != 0) {
				moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.BISHOP_CAPTURE,25);
            	moveCount++;       	
			} 
			else if ((toBB & board.WR) != 0) {
				moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.ROOK_CAPTURE,45);
            	moveCount++; 
			} 
			else {
				moves[moveCount] = MoveUtil.encodeMove(pos,toPos,0,MoveUtil.QUEEN_CAPTURE,85);
            	moveCount++; 
			}
        }
    }
    
    private static long computeBetween(int from, int to) {
        long mask = 0L;

        int fromRank = from / 8, fromFile = from % 8;
        int toRank = to / 8, toFile = to % 8;

        int rankDiff = Integer.compare(toRank, fromRank);
        int fileDiff = Integer.compare(toFile, fromFile);

        // Not aligned: return 0
        if (rankDiff == 0 && fileDiff == 0) return 0;
        if (!(rankDiff == 0 || fileDiff == 0 || Math.abs(rankDiff) == Math.abs(fileDiff))) return 0;

        int square = from + rankDiff * 8 + fileDiff;
        while (square != to) {
            mask |= 1L << square;
            square += rankDiff * 8 + fileDiff;
        }

        return mask;
    }
    
    public static void undoMove(BoardState board, int move, BoardMetaData bmd) {
    	int from = MoveUtil.getFromSquare(move);
        int to = MoveUtil.getToSquare(move);
        int flag = MoveUtil.getFlags(move);
        int captureFlag = MoveUtil.getCaptureFlags(move);

        long fromBB = 1L << from;
        long toBB = 1L << to;
        boolean isWhite = board.whiteToMove;
        
        // undoing the move black just did
        if (isWhite) {
        	switch (captureFlag) {
	    		case MoveUtil.PAWN_CAPTURE -> { board.WP |= toBB; }
	    		case MoveUtil.KNIGHT_CAPTURE -> { board.WN |= toBB; }
	    		case MoveUtil.BISHOP_CAPTURE -> { board.WB |= toBB; }
	    		case MoveUtil.ROOK_CAPTURE -> { board.WR |= toBB; }
	    		case MoveUtil.QUEEN_CAPTURE -> { board.WQ |= toBB; }
        	}
        	if (captureFlag != 0)
        		board.whitePieces |= toBB;
        	
        	if ((toBB & board.BP) != 0) {
        		board.BP ^= toBB;
        		board.BP |= fromBB;
        	}
        	else if ((toBB & board.BN) != 0) {
        		board.BN ^= toBB;
        		board.BN |= fromBB;
        	}
			else if ((toBB & board.BB) != 0) {
				board.BB ^= toBB;
        		board.BB |= fromBB;
			}
			else if ((toBB & board.BR) != 0) {
				board.BR ^= toBB;
        		board.BR |= fromBB;
			}
			else if ((toBB & board.BQ) != 0) {
				board.BQ ^= toBB;
        		board.BQ |= fromBB;
			}
			else {
				board.BK = fromBB;
			}
        	
        	board.blackPieces ^= toBB;
        	board.blackPieces ^= fromBB;
    		
        	switch (flag) {
        		case MoveUtil.EN_PASSANT -> {
        			board.WP |= toBB >>> 8;
        			board.whitePieces |= toBB >>> 8;
        		}
        		case MoveUtil.KING_CASTLE -> {
            		board.BR ^= (1L << 7);
            		board.BR ^= (1L << 5);
            		board.blackPieces ^= (1L << 7);
            		board.blackPieces ^= (1L << 5);
        		}
        		case MoveUtil.QUEEN_CASTLE -> {
            		board.BR ^= (1L << 0);
            		board.BR ^= (1L << 3);
            		board.blackPieces ^= (1L << 0);
            		board.blackPieces ^= (1L << 3);
        		}
        		case MoveUtil.PROMOTE_Q -> {
        			board.BQ ^= fromBB;
        			board.BP |= fromBB;
        		}
        		case MoveUtil.PROMOTE_R -> {
        			board.BR ^= fromBB;
        			board.BP |= fromBB;
        		}
        		case MoveUtil.PROMOTE_N -> {
        			board.BN ^= fromBB;
        			board.BP |= fromBB;
        		}
        		case MoveUtil.PROMOTE_B -> {
        			board.BB ^= fromBB;
        			board.BP |= fromBB;
        		}
        	}
        }
        // undoing the move white just did
        else {
        	switch (captureFlag) {
	    		case MoveUtil.PAWN_CAPTURE -> { board.BP |= toBB; }
	    		case MoveUtil.KNIGHT_CAPTURE -> { board.BN |= toBB; }
	    		case MoveUtil.BISHOP_CAPTURE -> { board.BB |= toBB; }
	    		case MoveUtil.ROOK_CAPTURE -> { board.BR |= toBB; }
	    		case MoveUtil.QUEEN_CAPTURE -> { board.BQ |= toBB; }
	    	}
	    	if (captureFlag != 0)
	    		board.blackPieces |= toBB;
	    	
	    	if ((toBB & board.WP) != 0) {
	    		board.WP ^= toBB;
	    		board.WP |= fromBB;
	    	}
	    	else if ((toBB & board.WN) != 0) {
	    		board.WN ^= toBB;
	    		board.WN |= fromBB;
	    	}
			else if ((toBB & board.WB) != 0) {
				board.WB ^= toBB;
	    		board.WB |= fromBB;
			}
			else if ((toBB & board.WR) != 0) {
				board.WR ^= toBB;
	    		board.WR |= fromBB;
			}
			else if ((toBB & board.WQ) != 0) {
				board.WQ ^= toBB;
	    		board.WQ |= fromBB;
			}
			else {
				board.WK = fromBB;
			}
	    	
	    	board.whitePieces ^= toBB;
	    	board.whitePieces ^= fromBB;
			
	    	switch (flag) {
	    		case MoveUtil.EN_PASSANT -> {
	    			board.BP |= toBB << 8;
	    			board.blackPieces |= toBB << 8;
	    		}
	    		case MoveUtil.KING_CASTLE -> {
	    			board.WR ^= (1L << 63);
	        		board.WR ^= (1L << 61);
	        		board.whitePieces ^= (1L << 63);
	        		board.whitePieces ^= (1L << 61);
	    		}
	    		case MoveUtil.QUEEN_CASTLE -> {
	    			board.WR ^= (1L << 56);
	        		board.WR ^= (1L << 59);
	        		board.whitePieces ^= (1L << 56);
	        		board.whitePieces ^= (1L << 59);
	    		}
	    		case MoveUtil.PROMOTE_Q -> {
	    			board.WQ ^= fromBB;
	    			board.WP |= fromBB;
	    		}
	    		case MoveUtil.PROMOTE_R -> {
	    			board.WR ^= fromBB;
	    			board.WP |= fromBB;
	    		}
	    		case MoveUtil.PROMOTE_N -> {
	    			board.WN ^= fromBB;
	    			board.WP |= fromBB;
	    		}
	    		case MoveUtil.PROMOTE_B -> {
	    			board.WB ^= fromBB;
	    			board.WP |= fromBB;
	    		}
	    	}
        }
        
        board.allPieces = board.blackPieces | board.whitePieces;
		board.whiteToMove = !isWhite;
		board.castlingRights = bmd.castlingRights;
		board.enPassantFile = bmd.enPassantFile;
		board.zobristHash = board.computeZobristHash();
		board.previousHashes.remove(board.zobristHash);
    }
    
    public static void applyMove(BoardState board, int move) {
        int from = MoveUtil.getFromSquare(move);
        int to = MoveUtil.getToSquare(move);
        int flag = MoveUtil.getFlags(move);
        int captureFlag = MoveUtil.getCaptureFlags(move);

        long fromBB = 1L << from;
        long toBB = 1L << to;
        boolean isWhite = board.whiteToMove;

        board.enPassantFile = -1;

        // === PIECE MOVEMENT AND BITBOARD UPDATES ===
        if (isWhite) {
            moveWhitePiece(board, fromBB, toBB);
            if (captureFlag != 0) {
                clearCapturedPiece(board, toBB, false);
            }
            handleSpecialWhite(board, flag, toBB);
        } else {
            moveBlackPiece(board, fromBB, toBB);
            if (captureFlag != 0) {
                clearCapturedPiece(board, toBB, true);
            }
            handleSpecialBlack(board, flag, toBB);
        }

        // === EN PASSANT UPDATE ===
        if (flag == MoveUtil.DOUBLE_PAWN_PUSH)
            board.enPassantFile = from % 8;

        board.allPieces = board.whitePieces | board.blackPieces;
        board.whiteToMove = !isWhite;
        board.previousHashes.add(board.zobristHash);
        board.zobristHash = board.computeZobristHash();
    }
    
    public static void makeMoves(BoardState board, String movesString) {
    	String[] moves = movesString.split(" ");
    	
    	for (String moveStr : moves) {
    		int file = moveStr.charAt(0) - 'a';
    	    int rank = 8 - (moveStr.charAt(1) - '0');
    	    int fromSquare = rank * 8 + file;
    	    
    	    file = moveStr.charAt(2) - 'a';
    	    rank = 8 - (moveStr.charAt(3) - '0');
    	    int toSquare = rank * 8 + file;
    	    
    	    int flag = 0;
    	    
    	    if (moveStr.length() == 5) {
                char promoChar = moveStr.charAt(4);
                switch (promoChar) {
                    case 'q' -> flag = MoveUtil.PROMOTE_Q;
                    case 'r' -> flag = MoveUtil.PROMOTE_R;
                    case 'b' -> flag = MoveUtil.PROMOTE_B;
                    case 'n' -> flag = MoveUtil.PROMOTE_N;
                }
            }
    	    
    	    long fromBB = 1L << fromSquare;
            boolean isWhitePawn = (board.WP & fromBB) != 0;
            boolean isBlackPawn = (board.BP & fromBB) != 0;

            if ((isWhitePawn || isBlackPawn) && Math.abs(fromSquare - toSquare) == 16) {
                flag = MoveUtil.DOUBLE_PAWN_PUSH;
            }
            
            if ((isWhitePawn || isBlackPawn)
                    && Math.abs(fromSquare % 8 - toSquare % 8) == 1     // moved diagonally
                    && ((board.allPieces & (1L << toSquare)) == 0) ){ // target square is empty
                flag = MoveUtil.EN_PASSANT;
            }
            
            boolean isWhiteKing = (board.WK & fromBB) != 0;
            boolean isBlackKing = (board.BK & fromBB) != 0;

            if (isWhiteKing) {
                if (fromSquare == 60 && toSquare == 62) flag = MoveUtil.KING_CASTLE;   // White O-O
                else if (fromSquare == 60 && toSquare == 58) flag = MoveUtil.QUEEN_CASTLE; // White O-O-O
            } else if (isBlackKing) {
                if (fromSquare == 4 && toSquare == 6) flag = MoveUtil.KING_CASTLE;    // Black O-O
                else if (fromSquare == 4 && toSquare == 2) flag = MoveUtil.QUEEN_CASTLE;  // Black O-O-O
            }
            
            long toBB = 1L << toSquare;
            int captureFlag = MoveUtil.NO_CAPTURE;
            
            if ((board.allPieces & toBB) != 0) {
                // Destination square is occupied
                if ((board.WP & toBB) != 0 || (board.BP & toBB) != 0)
                	captureFlag = MoveUtil.PAWN_CAPTURE;
                else if ((board.WN & toBB) != 0 || (board.BN & toBB) != 0)
                	captureFlag = MoveUtil.KNIGHT_CAPTURE;
                else if ((board.WB & toBB) != 0 || (board.BB & toBB) != 0)
                	captureFlag = MoveUtil.BISHOP_CAPTURE;
                else if ((board.WR & toBB) != 0 || (board.BR & toBB) != 0)
                	captureFlag = MoveUtil.ROOK_CAPTURE;
                else if ((board.WQ & toBB) != 0 || (board.BQ & toBB) != 0)
                	captureFlag = MoveUtil.QUEEN_CAPTURE;
            }
            
            int move = MoveUtil.encodeMove(fromSquare, toSquare, flag, captureFlag);
            applyMove(board, move);
    	}
    }
    
    public int getBookMove() {
		int littleMove = BookEntry.openingBookMove(zobristHash);
		if (littleMove == -1)
			return -1;
				
		int littleFromSquare = (littleMove >>> 6) & 0b111111;
		int littleToSquare = (littleMove >>> 0) & 0b111111;
		int littlePromote = (littleMove >>> 12) & 0b111;
		
		int file = littleFromSquare%8;
	    int rank = 7-(littleFromSquare/8);
	    int fromSquare = rank * 8 + file;
	    
	    file = littleToSquare%8;
	    rank = 7-(littleToSquare/8);
	    int toSquare = rank * 8 + file;
	    
	    int flag = 0;
	    if (littlePromote != 0) {
            switch (littlePromote) {
                case 4 -> flag = MoveUtil.PROMOTE_Q;
                case 3 -> flag = MoveUtil.PROMOTE_R;
                case 2 -> flag = MoveUtil.PROMOTE_B;
                case 1 -> flag = MoveUtil.PROMOTE_N;
            }
        }
	    
	    long fromBB = 1L << fromSquare;
        boolean isWhitePawn = (WP & fromBB) != 0;
        boolean isBlackPawn = (BP & fromBB) != 0;

        if ((isWhitePawn || isBlackPawn) && Math.abs(fromSquare - toSquare) == 16) {
            flag = MoveUtil.DOUBLE_PAWN_PUSH;
        }
        
        if ((isWhitePawn || isBlackPawn)
                && Math.abs(fromSquare % 8 - toSquare % 8) == 1     // moved diagonally
                && ((allPieces & (1L << toSquare)) == 0) ){ // target square is empty
            flag = MoveUtil.EN_PASSANT;
        }
        
        boolean isWhiteKing = (WK & fromBB) != 0;
        boolean isBlackKing = (BK & fromBB) != 0;

        if (isWhiteKing) {
            if (fromSquare == 60 && toSquare == 62) {
            	flag = MoveUtil.KING_CASTLE;   // White O-O
            }
            else if (fromSquare == 60 && toSquare == 63) {
            	flag = MoveUtil.KING_CASTLE;
            	toSquare = 62;
            }
            else if (fromSquare == 60 && toSquare == 58) {
            	flag = MoveUtil.QUEEN_CASTLE; // White O-O-O
            }
            else if (fromSquare == 60 && toSquare == 56) {
            	flag = MoveUtil.QUEEN_CASTLE;
            	toSquare = 58;
            }
            else if (fromSquare == 60 && toSquare == 57) {
            	flag = MoveUtil.QUEEN_CASTLE;
            	toSquare = 58;
            }
        } else if (isBlackKing) {
            if (fromSquare == 4 && toSquare == 6) {
            	flag = MoveUtil.KING_CASTLE;    // Black O-O
            }
            else if (fromSquare == 4 && toSquare == 7) {
            	flag = MoveUtil.KING_CASTLE;
            	toSquare = 6;
            }
            else if (fromSquare == 4 && toSquare == 2) {
            	flag = MoveUtil.QUEEN_CASTLE;  // Black O-O-O
            }
            else if (fromSquare == 4 && toSquare == 1) {
            	flag = MoveUtil.QUEEN_CASTLE;
            	toSquare = 2;
            }
            else if (fromSquare == 4 && toSquare == 0) {
            	flag = MoveUtil.QUEEN_CASTLE;
            	toSquare = 2;
            }
        }
        
        long toBB = 1L << toSquare;
        int captureFlag = MoveUtil.NO_CAPTURE;
        
        if ((allPieces & toBB) != 0) {
            // Destination square is occupied
            if ((WP & toBB) != 0 || (BP & toBB) != 0)
            	captureFlag = MoveUtil.PAWN_CAPTURE;
            else if ((WN & toBB) != 0 || (BN & toBB) != 0)
            	captureFlag = MoveUtil.KNIGHT_CAPTURE;
            else if ((WB & toBB) != 0 || (BB & toBB) != 0)
            	captureFlag = MoveUtil.BISHOP_CAPTURE;
            else if ((WR & toBB) != 0 || (BR & toBB) != 0)
            	captureFlag = MoveUtil.ROOK_CAPTURE;
            else if ((WQ & toBB) != 0 || (BQ & toBB) != 0)
            	captureFlag = MoveUtil.QUEEN_CAPTURE;
        }
        
        return MoveUtil.encodeMove(fromSquare, toSquare, flag, captureFlag);
	}

    private static void moveWhitePiece(BoardState b, long from, long to) {
        if ((from & b.WP) != 0) { 
        	b.WP |= to;
        	b.WP ^= from;
        }
        else if ((from & b.WB) != 0) {
        	b.WB |= to;
        	b.WB ^= from;
        }
        else if ((from & b.WN) != 0) {
        	b.WN |= to;
        	b.WN ^= from;
        }
        else if ((from & b.WR) != 0) {
        	if ((from & (1L << 63)) != 0)
        		b.castlingRights &= 0b1011;
        	else if ((from & (1L << 56)) != 0)
        		b.castlingRights &= 0b0111;
        	b.WR |= to;
        	b.WR ^= from;
        }
        else if ((from & b.WQ) != 0) {
        	b.WQ |= to;
        	b.WQ ^= from;
        }
        else { 
        	b.WK = to; 
        	b.castlingRights &= 0b0011;
        }
        b.whitePieces ^= from; b.whitePieces |= to;
    }

    private static void moveBlackPiece(BoardState b, long from, long to) {
        if ((from & b.BP) != 0) { 
        	b.BP |= to;
        	b.BP &= ~from;
        }
        else if ((from & b.BB) != 0) { 
        	b.BB |= to;
        	b.BB &= ~from;
        }
        else if ((from & b.BN) != 0) {
        	b.BN |= to;
        	b.BN &= ~from;
        }
        else if ((from & b.BR) != 0) {
        	b.BR |= to;
        	b.BR &= ~from;
        	if ((from & (1L << 7)) != 0)
        		b.castlingRights &= 0b1110;
        	else if ((from & (1L << 0)) != 0)
        		b.castlingRights &= 0b1101;
        }
        else if ((from & b.BQ) != 0) {
        	b.BQ |= to;
        	b.BQ &= ~from;
        }
        else { 
        	b.BK = to;
        	b.castlingRights &= 0b1100; 
        }  
        b.blackPieces ^= from; b.blackPieces |= to;
    }

    private static void clearCapturedPiece(BoardState b, long to, boolean white) {
        if (white) {
            b.WP &= ~to; b.WB &= ~to; b.WN &= ~to;
            b.WR &= ~to; b.WQ &= ~to;
            b.whitePieces ^= to;
        } else {
            b.BP &= ~to; b.BB &= ~to; b.BN &= ~to;
            b.BR &= ~to; b.BQ &= ~to;
            b.blackPieces ^= to;
        }
    }

    private static void handleSpecialWhite(BoardState b, int flag, long to) {
        switch (flag) {
            case MoveUtil.KING_CASTLE -> {
                b.WR ^= (1L << 63) ^ (1L << 61);
                b.whitePieces ^= (1L << 63) ^ (1L << 61);
            }
            case MoveUtil.QUEEN_CASTLE -> {
                b.WR ^= (1L << 56) ^ (1L << 59);
                b.whitePieces ^= (1L << 56) ^ (1L << 59);
            }
            case MoveUtil.EN_PASSANT -> {b.BP ^= to << 8; b.blackPieces ^= to << 8;}
            case MoveUtil.PROMOTE_Q -> { b.WP ^= to; b.WQ ^= to; }
            case MoveUtil.PROMOTE_R -> { b.WP ^= to; b.WR ^= to; }
            case MoveUtil.PROMOTE_B -> { b.WP ^= to; b.WB ^= to; }
            case MoveUtil.PROMOTE_N -> { b.WP ^= to; b.WN ^= to; }
        }
    }

    private static void handleSpecialBlack(BoardState b, int flag, long to) {
        switch (flag) {
            case MoveUtil.KING_CASTLE -> {
                b.BR ^= (1L << 7) ^ (1L << 5);
                b.blackPieces ^= (1L << 7) ^ (1L << 5);
            }
            case MoveUtil.QUEEN_CASTLE -> {
                b.BR ^= (1L << 0) ^ (1L << 3);
                b.blackPieces ^= (1L << 0) ^ (1L << 3);
            }
            case MoveUtil.EN_PASSANT -> {b.WP ^= to >>> 8; b.whitePieces^= to >>> 8;}
            case MoveUtil.PROMOTE_Q -> { b.BP ^= to; b.BQ ^= to; }
            case MoveUtil.PROMOTE_R -> { b.BP ^= to; b.BR ^= to; }
            case MoveUtil.PROMOTE_B -> { b.BP ^= to; b.BB ^= to; }
            case MoveUtil.PROMOTE_N -> { b.BP ^= to; b.BN ^= to; }
        }
    }

    
    private static boolean areAligned(int from, int to) {
        int fromRank = from / 8, fromFile = from % 8;
        int toRank = to / 8, toFile = to % 8;

        return fromRank == toRank || fromFile == toFile || Math.abs(fromRank - toRank) == Math.abs(fromFile - toFile);
    }

    public void printBoard() {
    	String[][] position = new String[8][8];
        for (String[] row : position)
            Arrays.fill(row," ");
        
        fillPiece(position, WP, "♙");
        fillPiece(position, WN, "♘");
        fillPiece(position, WB, "♗");
        fillPiece(position, WR, "♖");
        fillPiece(position, WQ, "♕");
        fillPiece(position, WK, "♔");
        fillPiece(position, BP, "♟");
        fillPiece(position, BN, "♞");
        fillPiece(position, BB, "♝");
        fillPiece(position, BR, "♜");
        fillPiece(position, BQ, "♛");
        fillPiece(position, BK, "♚");
        
        System.out.println("  _________________________________");
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (j == 0)
                    System.out.print(8-i + " ");
                System.out.print("| " + position[i][j] + " ");
                if (j == 7){
                    System.out.println("|");
                    System.out.println("  _________________________________");
                }
            }
        }
        System.out.println("    a   b   c   d   e   f   g   h");
    }
    
    public static void printBitBoard(long bitBoard){
        System.out.println("  _________________________________");
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (j == 0)
                    System.out.print(8-i + " ");
                if ((bitBoard & (1L << (i*8+j))) != 0){
                    System.out.print("| 1 ");
                }
                else{
                    System.out.print("| 0 ");
                }
                if (j == 7){
                    System.out.println("|");
                    System.out.println("  _________________________________");
                }
            }
        }
        System.out.println("    a   b   c   d   e   f   g   h");
    }
    
    private static void fillPiece(String[][] board, long bitboard, String piece){
        while (bitboard != 0){
            int pos = Long.numberOfTrailingZeros(bitboard);
            board[pos/8][pos%8] = piece;
            bitboard &= bitboard-1;
        }
    }
    
    public static boolean areEqual(BoardState a, BoardState b) {
    	return a.WP == b.WP &&
    	           a.WN == b.WN &&
    	           a.WB == b.WB &&
    	           a.WR == b.WR &&
    	           a.WQ == b.WQ &&
    	           a.WK == b.WK &&
    	           a.BP == b.BP &&
    	           a.BN == b.BN &&
    	           a.BB == b.BB &&
    	           a.BR == b.BR &&
    	           a.BQ == b.BQ &&
    	           a.BK == b.BK &&
    	           a.whitePieces == b.whitePieces &&
    	           a.blackPieces == b.blackPieces &&
    	           a.allPieces == b.allPieces &&
    	           a.whiteToMove == b.whiteToMove &&
    	           a.castlingRights == b.castlingRights &&
    	           a.enPassantFile == b.enPassantFile &&
    	           a.zobristHash == b.zobristHash;
    }
    
    public static BoardState loadFEN(String fen) {
    	long WP = 0L;
    	long WN = 0L;
    	long WB = 0L;
    	long WR = 0L;
    	long WQ = 0L;
    	long WK = 0L;
    	long BP = 0L;
    	long BN = 0L;
    	long BB = 0L;
    	long BR = 0L;
    	long BQ = 0L;
    	long BK = 0L;

        String[] parts = fen.split(" ");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid FEN string");
        }

        String[] ranks = parts[0].split("/");
        if (ranks.length != 8) {
            throw new IllegalArgumentException("Invalid FEN board part");
        }

        for (int rank = 0; rank < 8; rank++) {
            int file = 0;
            for (int i = 0; i < ranks[rank].length(); i++) {
                char c = ranks[rank].charAt(i);
                if (Character.isDigit(c)) {
                    file += c - '0';
                } else {
                    int sq = (rank) * 8 + file;
                    switch (c) {
                        case 'P': WP |= 1L << sq; break;
                        case 'N': WN |= 1L << sq; break;
                        case 'B': WB |= 1L << sq; break;
                        case 'R': WR |= 1L << sq; break;
                        case 'Q': WQ |= 1L << sq; break;
                        case 'K': WK |= 1L << sq; break;
                        case 'p': BP |= 1L << sq; break;
                        case 'n': BN |= 1L << sq; break;
                        case 'b': BB |= 1L << sq; break;
                        case 'r': BR |= 1L << sq; break;
                        case 'q': BQ |= 1L << sq; break;
                        case 'k': BK |= 1L << sq; break;
                        default:
                            throw new IllegalArgumentException("Invalid piece char in FEN: " + c);
                    }
                    file++;
                }
            }
            if (file != 8) {
                throw new IllegalArgumentException("Invalid FEN rank length");
            }
        }

        boolean whiteToMove = parts[1].equals("w");

        int castlingRights = 0;
        String castling = parts[2];
        if (!castling.equals("-")) {
            if (castling.contains("K")) castlingRights |= 4;  // White kingside
            if (castling.contains("Q")) castlingRights |= 8;  // White queenside
            if (castling.contains("k")) castlingRights |= 1;  // Black kingside
            if (castling.contains("q")) castlingRights |= 2;  // Black queenside
        }
        int enPassantFile = -1;
        String ep = parts[3];
        if (ep.equals("-")) {
        	enPassantFile = -1;
        } else {
        	enPassantFile = algebraicToSquare(ep)%8;
        }
        
        return new BoardState(WP, //WP
    		WN, //WN
    		WB, //WB
    		WR, //WR
    		WQ, //WQ
    		WK, //WK
    		BP, //BP
    		BN, //BN
    		BB, //BB
    		BR, //BR
    		BQ, //BQ
    		BK, //BK
    		WP|WN|WB|WR|WQ|WK, //whitePieces
    		BP|BN|BB|BR|BQ|BK, //blackPieces
    		WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK, //allPieces
    		castlingRights, //castlingRights
    		enPassantFile, //enPassantFile
    		whiteToMove //whiteToMove
    	);
    }
    
    public static int algebraicToSquare(String sq) {
        int file = sq.charAt(0) - 'a';
        int rank = sq.charAt(1) - '1';
        return rank * 8 + file;
    }
    
}
