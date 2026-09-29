package com.example.donatehub.config;

import com.example.donatehub.entity.*;
import com.example.donatehub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DriveRepository driveRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private RecipientRepository recipientRepository;

    @Autowired
    private DonatedItemRepository donatedItemRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initializing sample data...");

        // Clear existing data to prevent duplicates on restart
        donatedItemRepository.deleteAll();
        recipientRepository.deleteAll();
        donorRepository.deleteAll();
        driveRepository.deleteAll();

        // Reset auto-increment IDs
        jdbcTemplate.execute("ALTER TABLE donated_items AUTO_INCREMENT = 1");
        jdbcTemplate.execute("ALTER TABLE recipients AUTO_INCREMENT = 1");
        jdbcTemplate.execute("ALTER TABLE donors AUTO_INCREMENT = 1");
        jdbcTemplate.execute("ALTER TABLE drives AUTO_INCREMENT = 1");

        // 1. Create Drives
        Drive winterDrive = new Drive("Winter '24", LocalDate.of(2024, 11, 1), LocalDate.of(2024, 12, 31));
        Drive bookDrive = new Drive("Book Drive", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 9, 15));
        driveRepository.saveAll(List.of(winterDrive, bookDrive));

        // 2. Create Donors (using your requested names)
        Donor naruto = new Donor("Naruto Uzumaki", "naruto@hokage.com", "111-222-3333");
        Donor tanjiro = new Donor("Tanjiro Kamado", "tanjiro@demon-slayer.com", "444-555-6666");
        Donor obenai = new Donor("Obenai Iguro", "obenai@hashira.com", "777-888-9999");
        Donor tomiyoka = new Donor("Giyu Tomiyoka", "tomiyoka@hashira.com", "123-456-7890");
        Donor zenitsu = new Donor("Zenitsu Agatsuma", "zenitsu@demon-slayer.com", "098-765-4321");
        Donor muzan = new Donor("Muzan Kibutsuji", "muzan@infinity-castle.com", "666-666-6666");
        Donor sanemi = new Donor("Sanemi Shinazugawa", "sanemi@hashira.com", "333-222-1111");
        
        donorRepository.saveAll(List.of(naruto, tanjiro, obenai, tomiyoka, zenitsu, muzan, sanemi));

        // 3. Create Recipients
        Recipient orphanage = new Recipient("Hidden Leaf Orphanage", "Konoha", "Iruka Sensei");
        Recipient demonCorps = new Recipient("Demon Slayer Corps HQ", "Mt. Fujikasane", "Kagaya Ubuyashiki");
        recipientRepository.saveAll(List.of(orphanage, demonCorps));

        // 4. Create Donated Items
        DonatedItem item1 = new DonatedItem("Orange Jumpsuit", "clothes", ItemCondition.WORN, winterDrive, naruto);
        DonatedItem item2 = new DonatedItem("Checkered Haori", "clothes", ItemCondition.GOOD, winterDrive, tanjiro);
        DonatedItem item3 = new DonatedItem("Demon Slayer Training Manual", "books", ItemCondition.NEW, bookDrive, tomiyoka);
        DonatedItem item4 = new DonatedItem("Striped Haori", "clothes", ItemCondition.NEW, winterDrive, obenai);
        DonatedItem item5 = new DonatedItem("Yellow Haori", "clothes", ItemCondition.WORN, winterDrive, zenitsu);
        DonatedItem item6 = new DonatedItem("Fedora Hat", "clothes", ItemCondition.GOOD, winterDrive, muzan);

        // Distribute some items, leave some undistributed
        item1.setDistributed(true);
        item1.setRecipient(orphanage);

        item2.setDistributed(true);
        item2.setRecipient(demonCorps);

        // Items 3, 4, 5, 6 remain undistributed
        donatedItemRepository.saveAll(List.of(item1, item2, item3, item4, item5, item6));

        System.out.println("Sample data initialization complete!");
        System.out.println("Created 2 Drives, 7 Donors, 2 Recipients, and 6 Donated Items.");
    }
}
