# eBankify

# Introduction à Spring Security
Spring Security est un framework puissant et hautement personnalisable utilisé pour gérer les aspects de sécurité dans les applications Java basées sur Spring. Il fournit des mécanismes robustes pour l'authentification et l'autorisation, tout en s'intégrant facilement à d'autres composants de l'écosystème Spring.

## 1. Principales fonctionnalités de Spring Security
   - **Authentification :** Vérification des identités des utilisateurs.
   - **Autorisation :** Contrôle d'accès aux ressources en fonction des rôles ou des privilèges.
   - **Protection contre les attaques :** Prévention des menaces telles que le Cross-Site Scripting (XSS), le Cross-Site Request Forgery (CSRF) et les attaques par injection.
   - **Gestion des sessions :** Contrôle des sessions utilisateur, y compris les fonctionnalités de déconnexion et de limitation des connexions simultanées.
   - **Intégration avec OAuth2 et OpenID Connect :** Prise en charge des protocoles modernes pour l'authentification.
## 2. Structure de Spring Security
   ### 2.1 Filtre de sécurité
   Spring Security intercepte les requêtes HTTP à l’aide de filtres pour appliquer des règles de sécurité. Le point d'entrée principal est la classe SecurityFilterChain.

   ### 2.2 AuthenticationManager
Le gestionnaire d'authentification est responsable de la vérification des informations d'identification de l'utilisateur. Il utilise différents gestionnaires comme :

- **DaoAuthenticationProvider :** Vérification des utilisateurs via une base de données.
- **JwtAuthenticationProvider :** Authentification via JSON Web Tokens (JWT).
### 2.3 SecurityContext
Le SecurityContext stocke les informations de sécurité de l'utilisateur connecté, telles que ses rôles et ses détails.

## 3. Configurer Spring Security
   3.1 Configuration de base
   Avec Spring Boot, vous pouvez commencer rapidement avec une configuration minimale dans application.properties :

### properties
```properties
spring.security.user.name=admin
spring.security.user.password=password
```
### 3.2 Exemple de configuration personnalisée
Voici un exemple de classe de configuration pour sécuriser une API REST :

```java

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()  // Désactiver CSRF pour les API REST
            .authorizeRequests()
                .antMatchers("/public/**").permitAll()  // Accès libre
                .antMatchers("/admin/**").hasRole("ADMIN")  // Accès réservé
                .anyRequest().authenticated()  // Authentification requise
            .and()
            .httpBasic();  // Utilisation de l'authentification HTTP Basic
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                               .username("user")
                               .password("password")
                               .roles("USER")
                               .build();
        return new InMemoryUserDetailsManager(user);
    }
}
```
## 4. Principales annotations
- @EnableWebSecurity : Active la configuration Spring Security.
- @Secured : Limite l'accès en fonction des rôles, par exemple : @Secured("ROLE_ADMIN").
- @PreAuthorize : Définit des conditions d'accès basées sur des expressions SpEL, par exemple : @PreAuthorize("hasRole('USER')").
- @PostAuthorize : Vérifie les autorisations après l'exécution d'une méthode.
## 5. Gestion des utilisateurs et des rôles
   ### 5.1 Utilisation d'une base de données
   Configurer une source d'utilisateurs basée sur JDBC :

```java
@Bean
public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
manager.setUsersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?");
manager.setAuthoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?");
return manager;
}
```
### 5.2 Utilisation de JWT
Pour sécuriser une API avec JWT :

Générez un token lors de l'authentification.
Vérifiez le token pour chaque requête.
## 6. Protéger une application Web avec CSRF
   Le CSRF (Cross-Site Request Forgery) est activé par défaut dans Spring Security. Pour protéger une application :

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
```
7. Spring Security avec OAuth2
   Spring Security prend en charge OAuth2 pour la gestion de l'authentification basée sur des fournisseurs externes comme Google, GitHub, etc.

```java
@EnableWebSecurity
public class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.oauth2Login()
            .loginPage("/oauth2/authorization/google")
            .defaultSuccessUrl("/home", true);
    }
}
```
## 8. Tests avec Spring Security
   Spring Security intègre des outils de test pour valider la sécurité de vos applications :

```java
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/admin"))
               .andExpect(status().isUnauthorized());
    }
}
```
