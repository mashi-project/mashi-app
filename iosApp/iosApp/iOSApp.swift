import FirebaseCore
import FirebaseMessaging
import Shared
import SwiftUI
import UserNotifications

class AppDelegate: NSObject, UIApplicationDelegate, MessagingDelegate, UNUserNotificationCenterDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions:
            [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {

        FirebaseApp.configure()

        // Firebase Messaging
        Messaging.messaging().delegate = self

        // iOS notification delegate
        UNUserNotificationCenter.current().delegate = self

        // Register with APNs
        application.registerForRemoteNotifications()

        KoinInitIosKt.doInitKoinIos()
        SharedCode.shared.doInit(swiftSvgLoader: SvgImageLoader())

        KMPNotifier.shared.initialize(
            configuration: NotificationPlatformConfigurationIos(
                showPushNotification: true,
                askNotificationPermissionOnStart: true,
                notificationSoundName: nil
            ),
            extensions: [FirebasePush.shared]
        )

        return true
    }

    // APNs token
    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        Messaging.messaging().apnsToken = deviceToken

        print("✅ APNs token received")

        // Ask Firebase for the FCM token
        Messaging.messaging().token { token, error in
            if let error = error {
                print("❌ FCM token error: \(error)")
            } else if let token = token {
                print("🔥 FCM token: \(token)")
            }
        }
    }

    func application(
        _ application: UIApplication,
        didFailToRegisterForRemoteNotificationsWithError error: Error
    ) {
        print("❌ APNs registration failed: \(error)")
    }

    // FCM token updates
    func messaging(
        _ messaging: Messaging,
        didReceiveRegistrationToken fcmToken: String?
    ) {
        print("🔥 FCM registration token: \(fcmToken ?? "nil")")

        // IMPORTANT:
        // Send this token to your backend/database.
    }

    // Foreground notification
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler:
            @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        print("📩 Notification received in foreground")
        print(notification.request.content.userInfo)

        completionHandler([.banner, .sound, .badge])
    }

    // Notification tapped
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        print("👆 Notification tapped")
        print(response.notification.request.content.userInfo)

        completionHandler()
    }

    func application(
        _ application: UIApplication,
        didReceiveRemoteNotification userInfo: [AnyHashable: Any]
    ) async -> UIBackgroundFetchResult {

        print("📨 Remote notification received")
        print(userInfo)

        KMPNotifier.shared.onApplicationDidReceiveRemoteNotification(
            userInfo: userInfo
        )

        return .newData
    }
}

@main
struct iOSApp: App {

    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var delegate

    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    SupabaseHandlerIosKt.handleDeeplinks(url: url)
                }
        }
    }
}
