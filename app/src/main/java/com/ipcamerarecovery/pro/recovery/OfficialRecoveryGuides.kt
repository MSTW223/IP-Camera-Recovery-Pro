package com.ipcamerarecovery.pro.recovery

import com.ipcamerarecovery.pro.model.Manufacturer
import com.ipcamerarecovery.pro.model.RecoveryProcedure
import com.ipcamerarecovery.pro.model.RecoveryStep

object OfficialRecoveryGuides {

    val PROCEDURES: List<RecoveryProcedure> = listOf(
        RecoveryProcedure(
            id = "hik_xml_sadp",
            manufacturer = Manufacturer.HIKVISION,
            titleEn = "Hikvision SADP XML Export & Key Import",
            titleAr = "إعادة ضبط هايكفيجن عبر تصدير ملف XML بأداة SADP",
            summaryEn = "Official cryptographic key exchange using Hikvision SADP Tool. Exports DeviceKey.xml and imports the vendor-signed Encrypt.xml.",
            summaryAr = "إجراء التشفير المعتمد من هايكفيجن عبر تصدير ملف DeviceKey.xml واستيراد مفتاح التشفير Encrypt.xml المعتمد.",
            officialSoftware = "Hikvision SADP Tool v3.1+",
            steps = listOf(
                RecoveryStep(
                    stepNumber = 1,
                    titleEn = "Connect on Same Layer-2 Broadcast Domain",
                    titleAr = "الاتصال على نفس نطاق البث المحلي (Layer-2)",
                    descriptionEn = "Connect your Android device or PC to the same physical switch or Wi-Fi network as the target Hikvision camera. Do NOT traverse layer-3 routed hops.",
                    descriptionAr = "قم بربط هاتفك أو حاسوبك بنفس السويتش أو شبكة الواي فاي المتصلة بها الكاميرا دون وجود راوتر وسيط."
                ),
                RecoveryStep(
                    stepNumber = 2,
                    titleEn = "Export DeviceKey.xml via SADP",
                    titleAr = "تصدير ملف DeviceKey.xml من برنامج SADP",
                    descriptionEn = "Select the target camera in SADP, click 'Forgot Password' at the bottom right, select 'Export XML', and save DeviceKey.xml to your storage.",
                    descriptionAr = "حدد الكاميرا في برنامج SADP، واضغط على 'Forgot Password' في أسفل اليمين، واختر 'Export XML' واحفظ الملف.",
                    warningEn = "CRITICAL: Do NOT power cycle or reboot the camera after exporting! A reboot invalidates the session key.",
                    warningAr = "تنبيه هام جداً: لا تقم بإعادة تشغيل أو فصل الكهرباء عن الكاميرا بعد تصدير الملف لأن ذلك يبطل مفتاح الجلسة."
                ),
                RecoveryStep(
                    stepNumber = 3,
                    titleEn = "Submit to Authorized Distributor Portal",
                    titleAr = "إرسال الملف إلى بوابة الموزع المعتمد",
                    descriptionEn = "Email the XML file and camera serial number photo to your official Hikvision distributor support desk.",
                    descriptionAr = "أرسل ملف XML مع صورة ملصق الرقم التسلسلي للكاميرا إلى الدعم الفني للموزع المعتمد لتوليد مفتاح فك القفل."
                ),
                RecoveryStep(
                    stepNumber = 4,
                    titleEn = "Import Encrypt.xml and Set New Password",
                    titleAr = "استيراد ملف Encrypt.xml وتعيين كلمة المرور الجديدة",
                    descriptionEn = "Select 'Import File', choose the received Encrypt.xml, enter your new complex password (min 8 chars, mixed case, numbers & symbols), and click Confirm.",
                    descriptionAr = "اختر 'Import File' واستورد ملف Encrypt.xml الذي استلمته، ثم أدخل كلمة المرور الجديدة المعقدة واضغط تأكيد."
                )
            ),
            hardwareButtonInstructionsEn = "Hikvision Turret/Dome: Unscrew the microSD card cover. Locate the small tactile button next to the SD slot. Hold button down, connect 12V DC / PoE power, keep holding for 15-20 seconds until IR LEDs flash twice.",
            hardwareButtonInstructionsAr = "كاميرات هايكفيجن الدوم/البرجية: فك غطاء بطاقة الذاكرة microSD. ستجد زر إعادة ضبط صغير بجانب المنفذ. اضغط الزر باستمرار ثم وصّل الكهرباء واستمر بالضغط لمدة 15-20 ثانية حتى تومض لمبات الأشعة مرتين."
        ),
        RecoveryProcedure(
            id = "dahua_xml_configtool",
            manufacturer = Manufacturer.DAHUA,
            titleEn = "Dahua ConfigTool XML / QR Code Reset",
            titleAr = "إعادة ضبط داهوا عبر ملف XML أو رمز QR بأداة ConfigTool",
            summaryEn = "Official Dahua reset procedure using ConfigTool password reset wizard with cryptographic XML handshake or DMSS app scan.",
            summaryAr = "إجراء داهوا الرسمي باستخدام معالج إعادة الضبط في برنامج ConfigTool عبر تبادل ملف XML أو مسح الرمز بتطبيق DMSS.",
            officialSoftware = "Dahua ConfigTool v5.0+",
            steps = listOf(
                RecoveryStep(
                    stepNumber = 1,
                    titleEn = "Launch Dahua ConfigTool Search",
                    titleAr = "تشغيل البحث في برنامج Dahua ConfigTool",
                    descriptionEn = "Scan local network in ConfigTool. Highlight the locked Dahua camera and click the 'Password Reset' icon.",
                    descriptionAr = "قم بفحص الشبكة في ConfigTool، وحدد كاميرا داهوا المقفلة واضغط على أيقونة 'Password Reset'."
                ),
                RecoveryStep(
                    stepNumber = 2,
                    titleEn = "Choose Reset Mode (XML File or QR Code)",
                    titleAr = "اختيار وضع إعادة الضبط (ملف XML أو رمز QR)",
                    descriptionEn = "If security email was linked during initialization, choose QR Code and scan with authorized DMSS app. Otherwise, select 'Export XML'.",
                    descriptionAr = "إذا تم ربط بريد أمان مسبقاً، اختر QR Code وامسحه بتطبيق DMSS. أو اختر 'Export XML' لتصدير ملف الطلب."
                ),
                RecoveryStep(
                    stepNumber = 3,
                    titleEn = "Request Result.xml from Dahua Support",
                    titleAr = "طلب ملف Result.xml من الدعم الفني لداهوا",
                    descriptionEn = "Send ExportFile.xml with invoice or authorized technician credentials to Dahua RMA/distributor portal.",
                    descriptionAr = "أرسل ملف ExportFile.xml مع إثبات الشراء أو تفويض الصيانة المعتمد للحصول على ملف النتيجة Result.xml."
                ),
                RecoveryStep(
                    stepNumber = 4,
                    titleEn = "Import Result.xml and Initialize",
                    titleAr = "استيراد ملف Result.xml وتهيئة كلمة المرور",
                    descriptionEn = "Select 'Open' to load Result.xml, enter new password conforming to Dahua security policy (8-32 characters), and save.",
                    descriptionAr = "استورد ملف Result.xml وأدخل كلمة المرور الجديدة المطابقة لسياسات الأمان (8-32 خانة) واضغط حفظ."
                )
            ),
            hardwareButtonInstructionsEn = "Dahua Eyeball & Bullet Cameras: Remove weather seal screw cap. Press and hold the physical reset microswitch for 10-15 seconds while powered on. The red/green status LED will turn red and reboot to factory default IP (192.168.1.108).",
            hardwareButtonInstructionsAr = "كاميرات داهوا البوليت والآيبول: أزل الغطاء الواقي من الماء. اضغط باستمرار على زر الريست لمدة 10-15 ثانية أثناء تشغيل الكهرباء. سيتحول مؤشر LED للأحمر ويعاد التشغيل إلى عنوان المصنع الافتراضي (192.168.1.108)."
        ),
        RecoveryProcedure(
            id = "hardware_reset_matrix",
            manufacturer = Manufacturer.GENERIC_ONVIF,
            titleEn = "Physical Hardware Reset Switch Matrix",
            titleAr = "دليل مواقع أزرار إعادة الضبط الفيزيائية لجميع الموديلات",
            summaryEn = "Step-by-step physical reset timings and button locations across Dahua, Hikvision, and Uniview form factors.",
            summaryAr = "مواقع أزرار إعادة الضبط وتوقيتات الضغط الدقيقة لكاميرات داهوا وهايكفيجن ويونيفيو بمختلف أشكالها الهندسية.",
            officialSoftware = "No software required (Physical Access)",
            steps = listOf(
                RecoveryStep(
                    stepNumber = 1,
                    titleEn = "Inspect Camera Casing & SD Compartment",
                    titleAr = "فحص هيكل الكاميرا وغطاء الذاكرة",
                    descriptionEn = "In 85% of modern IP cameras, the physical reset switch is located under the weatherproof access panel containing the MicroSD slot.",
                    descriptionAr = "في 85% من الكاميرات الحديثة، يوجد زر الضبط داخل الحجرة المحمية ضد العوامل الجوية بجانب منفذ كرت الذاكرة."
                ),
                RecoveryStep(
                    stepNumber = 2,
                    titleEn = "Power Cycle Timing Method (Cold Reset)",
                    titleAr = "طريقة التوقيت مع إعادة التشغيل (الضبط البارد)",
                    descriptionEn = "1. Unplug PoE or 12V DC power.\n2. Press and hold the reset button firmly.\n3. Reconnect power while KEEPING button pressed.\n4. Maintain hold for exactly 20 seconds, then release.\n5. Wait 2 minutes for complete reboot.",
                    descriptionAr = "1. افصل كابل PoE أو محول 12V.\n2. اضغط زر الريست باستمرار.\n3. أعد توصيل الطاقة مع الاستمرار بالضغط على الزر.\n4. استمر بالضغط لمدة 20 ثانية كاملة ثم اترك الزر.\n5. انتظر دقيقتين حتى تكتمل إعادة التشغيل."
                ),
                RecoveryStep(
                    stepNumber = 3,
                    titleEn = "Post-Reset Verification",
                    titleAr = "التحقق بعد إعادة الضبط",
                    descriptionEn = "Run the Discovery scanner in this app. The camera will appear with default factory IP (Hikvision: 192.168.1.64 inactive; Dahua: 192.168.1.108 uninitialized).",
                    descriptionAr = "قم بتشغيل فحص الكشف في هذا التطبيق. ستظهر الكاميرا بعنوان المصنع الافتراضي (هايكفيجن: 192.168.1.64 غير مفعلة، داهوا: 192.168.1.108 غير مهيأة)."
                )
            ),
            hardwareButtonInstructionsEn = "Warning: Some Dahua PTZ speed domes require opening the top cover and pressing the reset jumper pins JP1 for 5 seconds.",
            hardwareButtonInstructionsAr = "تنبيه: بعض كاميرات داهوا المتحركة PTZ تتطلب فك الغطاء العلوي ولمس قطبي التوصيل JP1 لمدة 5 ثوان."
        )
    )
}
